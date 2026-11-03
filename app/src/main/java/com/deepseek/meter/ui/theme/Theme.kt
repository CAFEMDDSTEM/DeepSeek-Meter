package com.deepseek.meter.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val RemainingColor = Color(0xFF0F766E)
val UsedColor = Color(0xFFB45309)
val TodayColor = Color(0xFFC2410C)
val AccentBlue = Color(0xFF1D4ED8)
val CacheMissColor = Color(0xFF2563EB)
val CacheHitColor = Color(0xFF0F766E)
val OutputColor = Color(0xFF7C3AED)

private val LightColors = lightColorScheme(
    primary = AccentBlue,
    onPrimary = Color.White,
    secondary = RemainingColor,
    tertiary = UsedColor,
    background = Color(0xFFF7F8FA),
    surface = Color.White,
    surfaceVariant = Color(0xFFEEF1F5),
    onSurface = Color(0xFF171A1F),
    onSurfaceVariant = Color(0xFF4B5563),
    error = Color(0xFFB91C1C),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8AB4FF),
    onPrimary = Color(0xFF002B72),
    secondary = Color(0xFF72D6C9),
    tertiary = Color(0xFFF5B56B),
    background = Color(0xFF111318),
    surface = Color(0xFF191C22),
    surfaceVariant = Color(0xFF252A33),
    onSurface = Color(0xFFE6E8EC),
    onSurfaceVariant = Color(0xFFC2C7D0),
    error = Color(0xFFFFB4AB),
)

@Composable
fun DeepSeekMeterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
