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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deepseek.meter.ui.localization.LocalAppStrings
import com.deepseek.meter.ui.localization.S
import com.deepseek.meter.util.formatMoney

@Composable
fun BalanceRing(
    rechargeRemaining: Double,
    bonusRemaining: Double,
    used: Double,
    today: Double,
    currency: String,
    rechargeColor: Color,
    bonusColor: Color,
    usedColor: Color,
    todayColor: Color,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current
    val total = (rechargeRemaining + bonusRemaining + used).coerceAtLeast(0.0)
    val rechargeRatio = ratio(rechargeRemaining, total)
    val bonusRatio = ratio(bonusRemaining, total)
    val usedRatio = ratio(used, total)
    val todayRatio = ratio(today, total)
    val usedStartAngle = -90f + (360.0 * (rechargeRatio + bonusRatio)).toFloat()
    val description = strings.format(
        S.otherCurrencySummary,
        "currency" to currency,
        "remaining" to formatMoney(rechargeRemaining + bonusRemaining, currency),
        "recharge" to formatMoney(rechargeRemaining, currency),
        "bonus" to formatMoney(bonusRemaining, currency),
        "used" to formatMoney(used, currency),
    ) + ", " + strings[S.todayUsage] + ": " + formatMoney(today, currency)
    val trackColor = MaterialTheme.colorScheme.surfaceVariant

    Box(
        modifier = modifier
            .size(236.dp)
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(236.dp)) {
            val outerStroke = 24.dp.toPx()
            val innerStroke = 11.dp.toPx()
            val diameter = size.minDimension - outerStroke
            val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
            val arcSize = Size(diameter, diameter)

            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = outerStroke, cap = StrokeCap.Butt),
            )

            var startAngle = -90f
            drawArc(
                color = rechargeColor,
                startAngle = startAngle,
                sweepAngle = (360.0 * rechargeRatio).toFloat(),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = outerStroke, cap = StrokeCap.Butt),
            )
            startAngle += (360.0 * rechargeRatio).toFloat()
            drawArc(
                color = bonusColor,
                startAngle = startAngle,
                sweepAngle = (360.0 * bonusRatio).toFloat(),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = outerStroke, cap = StrokeCap.Butt),
            )
            startAngle += (360.0 * bonusRatio).toFloat()
            drawArc(
                color = usedColor,
                startAngle = startAngle,
                sweepAngle = (360.0 * usedRatio).toFloat(),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = outerStroke, cap = StrokeCap.Butt),
            )

            val innerInset = outerStroke + innerStroke
            val innerDiameter = size.minDimension - innerInset * 2f
            val innerTopLeft = Offset((size.width - innerDiameter) / 2f, (size.height - innerDiameter) / 2f)
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = innerTopLeft,
                size = Size(innerDiameter, innerDiameter),
                style = Stroke(width = innerStroke, cap = StrokeCap.Butt),
            )
            drawArc(
                color = todayColor,
                startAngle = usedStartAngle,
                sweepAngle = (360.0 * todayRatio).toFloat(),
                useCenter = false,
                topLeft = innerTopLeft,
                size = Size(innerDiameter, innerDiameter),
                style = Stroke(width = innerStroke, cap = StrokeCap.Butt),
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                formatMoney(rechargeRemaining + bonusRemaining, currency),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                strings[S.currentTotalRemaining],
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun BalanceLegend(
    rechargeRemaining: Double,
    bonusRemaining: Double,
    used: Double,
    today: Double,
    currency: String,
    rechargeColor: Color,
    bonusColor: Color,
    usedColor: Color,
    todayColor: Color,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current
    val remaining = rechargeRemaining + bonusRemaining
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(9.dp)) {
        LegendRow(
            label = strings[S.remaining],
            value = formatMoney(remaining, currency),
            color = rechargeColor,
            note = strings[S.totalRemainingFormula],
            emphasized = true,
        )
        LegendRow(
            label = strings[S.rechargeBalance],
            value = formatMoney(rechargeRemaining, currency),
            color = rechargeColor,
            indent = true,
        )
        LegendRow(
            label = strings[S.bonusBalance],
            value = formatMoney(bonusRemaining, currency),
            color = bonusColor,
            indent = true,
        )
        LegendRow(
            label = strings[S.used],
            value = formatMoney(used, currency),
            color = usedColor,
        )
        LegendRow(
            label = strings[S.todayUsage],
            value = formatMoney(today, currency),
            color = todayColor,
            note = strings[S.todayIncluded],
            emphasized = true,
        )
    }
}

@Composable
private fun LegendRow(
    label: String,
    value: String,
    color: Color,
    note: String? = null,
    emphasized: Boolean = false,
    indent: Boolean = false,
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Spacer(
            modifier = Modifier
                .width(5.dp)
                .height(if (emphasized) 38.dp else 30.dp)
                .background(color, RoundedCornerShape(2.dp)),
        )
        Spacer(Modifier.width(if (indent) 20.dp else 10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Medium)
            if (note != null) {
                Text(note, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

private fun ratio(value: Double, total: Double): Double =
    if (total > 0.0) (value / total).coerceIn(0.0, 1.0) else 0.0
