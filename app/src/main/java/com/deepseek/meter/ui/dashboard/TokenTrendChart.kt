package com.deepseek.meter.ui.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.deepseek.meter.data.model.TrendPoint
import com.deepseek.meter.ui.localization.LocalAppStrings
import com.deepseek.meter.ui.localization.S
import com.deepseek.meter.ui.theme.CacheHitColor
import com.deepseek.meter.ui.theme.CacheMissColor
import com.deepseek.meter.ui.theme.OutputColor
import com.deepseek.meter.util.formatToken

@Composable
fun TokenTrendChart(
    points: List<TrendPoint>,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current
    val maxValue = points.maxOfOrNull { it.totalTokens }?.coerceAtLeast(1L) ?: 1L
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            TrendLegend("${strings[S.input]} (${strings[S.cacheHit]}-)", CacheMissColor)
            TrendLegend(strings[S.cacheHit], CacheHitColor)
            TrendLegend(strings[S.output], OutputColor)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .width(46.dp)
                    .height(154.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End,
            ) {
                Text(formatToken(maxValue), style = MaterialTheme.typography.labelSmall)
                Text("0", style = MaterialTheme.typography.labelSmall)
            }
            LazyRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(points, key = { it.date.toString() }) { point ->
                    TrendDay(point = point, maxValue = maxValue)
                }
            }
        }
    }
}

@Composable
private fun TrendDay(point: TrendPoint, maxValue: Long) {
    val strings = LocalAppStrings.current
    val description = "${point.date}, ${strings[S.input]} ${formatToken(point.inputTokens)}, ${strings[S.cacheHit]} ${formatToken(point.promptCacheHitTokens)}, ${strings[S.output]} ${formatToken(point.outputTokens)}"
    Column(
        modifier = Modifier
            .width(28.dp)
            .semantics { contentDescription = description },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Canvas(
            modifier = Modifier
                .height(136.dp)
                .width(22.dp)
                .clip(RoundedCornerShape(3.dp)),
        ) {
            val max = maxValue.coerceAtLeast(1L).toFloat()
            val missHeight = size.height * (point.promptCacheMissTokens / max)
            val hitHeight = size.height * (point.promptCacheHitTokens / max)
            val outputHeight = size.height * (point.outputTokens / max)
            var bottom = size.height

            listOf(
                CacheMissColor to missHeight,
                CacheHitColor to hitHeight,
                OutputColor to outputHeight,
            ).forEach { (color, height) ->
                if (height > 0f) {
                    drawRect(
                        color = color,
                        topLeft = Offset(0f, bottom - height),
                        size = Size(size.width, height),
                    )
                    bottom -= height
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Text("%02d".format(point.date.dayOfMonth), style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun TrendLegend(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
            Modifier
                .width(12.dp)
                .height(10.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color),
        )
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}
