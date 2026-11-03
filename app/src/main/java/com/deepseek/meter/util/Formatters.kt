package com.deepseek.meter.util

import java.text.DecimalFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val moneyFormat = DecimalFormat("#,##0.00")
private val timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss")

fun formatMoney(value: Double, currency: String): String {
    val symbol = when (currency.uppercase(Locale.ROOT)) {
        "CNY" -> "¥"
        "USD" -> "$"
        else -> ""
    }
    val code = currency.uppercase(Locale.ROOT)
    return if (symbol.isNotEmpty()) "$symbol${moneyFormat.format(value)}" else "${moneyFormat.format(value)} $code"
}

fun formatAmount(value: Double): String = moneyFormat.format(value)

fun formatToken(value: Long): String = when {
    value >= 1_000_000_000L -> "${"%.2f".format(Locale.ROOT, value / 1_000_000_000.0)}B"
    value >= 1_000_000L -> "${"%.2f".format(Locale.ROOT, value / 1_000_000.0)}M"
    value >= 1_000L -> "${"%.1f".format(Locale.ROOT, value / 1_000.0)}K"
    else -> value.toString()
}

fun formatCount(value: Long): String = DecimalFormat("#,##0").format(value)

fun formatTime(instant: Instant): String =
    timeFormat.format(instant.atZone(ZoneId.systemDefault()))
