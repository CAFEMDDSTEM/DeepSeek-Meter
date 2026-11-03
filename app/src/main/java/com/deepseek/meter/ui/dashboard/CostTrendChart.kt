package com.deepseek.meter.ui.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deepseek.meter.data.model.CostTrendPoint
import com.deepseek.meter.ui.localization.LocalAppStrings
import com.deepseek.meter.ui.localization.S
import com.deepseek.meter.util.formatAmount
import com.deepseek.meter.util.formatMoney
import java.util.Locale

private enum class CostDimension { ApiKey, Model, Combined }

private val COST_COLORS = listOf(
    Color(0xFF2563EB), Color(0xFF0F766E), Color(0xFF7C3AED), Color(0xFFB45309),
    Color(0xFFC2410C), Color(0xFF0891B2), Color(0xFF4D7C0F), Color(0xFF334155),
    Color(0xFFBE185D), Color(0xFF15803D),
)

@Composable
fun CostTrendChart(
    points: List<CostTrendPoint>,
    currency: String,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current
    var dimension by remember { mutableStateOf(CostDimension.ApiKey) }
    val keys = when (dimension) {
        CostDimension.ApiKey -> points.flatMap { it.byApiKey.keys }.distinct()
        CostDimension.Model -> points.flatMap { it.byModel.keys }.distinct()
        CostDimension.Combined -> points.flatMap { it.byApiKeyModel.keys }.distinct()
    }.sorted()
    val totals = keys.associateWith { key ->
        points.sumOf { point ->
            when (dimension) {
                CostDimension.ApiKey -> point.byApiKey[key] ?: 0.0
                CostDimension.Model -> point.byModel[key] ?: 0.0
                CostDimension.Combined -> point.byApiKeyModel[key] ?: 0.0
            }
        }
    }
    val displayKeys = keys.sortedByDescending { totals[it] ?: 0.0 }
    val maxValue = points.maxOfOrNull { it.total }?.coerceAtLeast(0.01) ?: 0.01
    val total = points.sumOf { it.total }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FilterChip(
                selected = dimension == CostDimension.ApiKey,
                onClick = { dimension = CostDimension.ApiKey },
                label = { Text(strings[S.byApiKey]) },
            )
            FilterChip(
                selected = dimension == CostDimension.Model,
                onClick = { dimension = CostDimension.Model },
                label = { Text(strings[S.byModel]) },
            )
            FilterChip(
                selected = dimension == CostDimension.Combined,
                onClick = { dimension = CostDimension.Combined },
                label = { Text("API Key × " + strings[S.byModel]) },
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(strings[S.chartTotal], style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(formatMoney(total, currency), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }
        }

        if (total <= 0.0 || displayKeys.isEmpty()) {
            Text(strings[S.noCostData], style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            return@Column
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .width(58.dp)
                    .height(154.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End,
            ) {
                Text(formatAmount(maxValue), style = MaterialTheme.typography.labelSmall)
                Text("0", style = MaterialTheme.typography.labelSmall)
            }
            LazyRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(points, key = { it.date.toString() }) { point ->
                    val values = when (dimension) {
                        CostDimension.ApiKey -> point.byApiKey
                        CostDimension.Model -> point.byModel
                        CostDimension.Combined -> point.byApiKeyModel
                    }
                    CostDayBar(
                        dateDay = point.date.dayOfMonth,
                        values = values,
                        keys = displayKeys,
                        colors = displayKeys.mapIndexed { index, _ -> COST_COLORS[index % COST_COLORS.size] },
                        maxValue = maxValue,
                        currency = currency,
                    )
                }
            }
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(displayKeys) { key ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        Modifier
                            .width(12.dp)
                            .height(10.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(COST_COLORS[displayKeys.indexOf(key) % COST_COLORS.size]),
                    )
                    Text(key, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun CostDayBar(
    dateDay: Int,
    values: Map<String, Double>,
    keys: List<String>,
    colors: List<Color>,
    maxValue: Double,
    currency: String,
) {
    val total = values.values.sum()
    val description = "${String.format(Locale.ROOT, "%02d", dateDay)}: ${formatMoney(total, currency)}"
    Column(
        modifier = Modifier
            .width(30.dp)
            .semantics { contentDescription = description },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Canvas(
            modifier = Modifier
                .height(136.dp)
                .width(24.dp)
                .clip(RoundedCornerShape(3.dp)),
        ) {
            var bottom = size.height
            keys.forEachIndexed { index, key ->
                val height = size.height * ((values[key] ?: 0.0) / maxValue).toFloat()
                if (height > 0f) {
                    drawRect(
                        color = colors[index],
                        topLeft = Offset(0f, bottom - height),
                        size = Size(size.width, height),
                    )
                    bottom -= height
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Text("%02d".format(dateDay), style = MaterialTheme.typography.labelSmall)
    }
}
