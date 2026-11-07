package com.deepseek.meter.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.Locale
import com.deepseek.meter.data.model.AppSettings
import com.deepseek.meter.ui.MainUiState
import com.deepseek.meter.ui.localization.LocalAppStrings
import com.deepseek.meter.ui.localization.S
import com.deepseek.meter.ui.localization.languageName
import com.deepseek.meter.ui.localization.supportedLanguages
import com.deepseek.meter.util.formatAmount

@Composable
fun SettingsScreen(
    state: MainUiState,
    notificationPermissionGranted: Boolean,
    onRefreshIntervalChange: (Int) -> Unit,
    onTodayAlertEnabledChange: (Boolean) -> Unit,
    onTodayAlertThresholdChange: (Double) -> Unit,
    onLowBalanceAlertEnabledChange: (Boolean) -> Unit,
    onLowBalanceThresholdChange: (Double) -> Unit,
    onRingRechargeColorChange: (Long) -> Unit,
    onRingBonusColorChange: (Long) -> Unit,
    onRingUsedColorChange: (Long) -> Unit,
    onRingTodayColorChange: (Long) -> Unit,
    onResetRingColors: () -> Unit,
    onLanguageChange: (String) -> Unit,
    onCheckUpdates: () -> Unit,
    updateChecking: Boolean,
    updateMessage: String?,
    onLogin: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current
    val settings = state.settings
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(strings[S.settings], style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(strings[S.language], style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(strings[S.languageDesc], style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LanguageSelector(selectedCode = settings.languageCode, onSelected = onLanguageChange)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(strings[S.checkUpdates], style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    strings.format(S.versionLabel, "0" to com.deepseek.meter.BuildConfig.VERSION_NAME),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Button(onClick = onCheckUpdates, enabled = !updateChecking) {
                    Text(if (updateChecking) strings[S.checkingUpdate] else strings[S.checkUpdates])
                }
                if (updateMessage != null) {
                    Text(updateMessage, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(strings[S.autoRefresh], style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    strings[S.autoRefreshDesc],
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                RefreshIntervalSelector(
                    selectedSeconds = settings.refreshIntervalSeconds,
                    strings = strings,
                    onSelected = onRefreshIntervalChange,
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(strings[S.todayAlert], style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                SwitchRow(
                    label = strings[S.enableTodayAlert],
                    checked = settings.todayAlertEnabled,
                    onCheckedChange = onTodayAlertEnabledChange,
                )
                ThresholdInput(
                    label = strings.format(S.todayThreshold, "0" to (state.dashboard?.primaryCurrency ?: "CNY")),
                    value = settings.todayAlertThreshold,
                    onValueChange = onTodayAlertThresholdChange,
                )
                if (!notificationPermissionGranted) {
                    Text(
                        strings[S.notificationPermission],
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                Text(
                    strings[S.oncePerDay],
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(strings[S.lowBalanceAlert], style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                SwitchRow(
                    label = strings[S.enableLowBalance],
                    checked = settings.lowBalanceAlertEnabled,
                    onCheckedChange = onLowBalanceAlertEnabledChange,
                )
                ThresholdInput(
                    label = strings.format(S.lowThreshold, "0" to (state.dashboard?.primaryCurrency ?: "CNY")),
                    value = settings.lowBalanceThreshold,
                    onValueChange = onLowBalanceThresholdChange,
                )
                Text(
                    strings[S.lowBalanceDesc],
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(strings[S.ringColors], style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    strings[S.ringColorsDesc],
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                RingColorSetting(
                    label = strings[S.rechargeBalance],
                    color = settings.ringRechargeColor,
                    onColorChange = onRingRechargeColorChange,
                )
                RingColorSetting(
                    label = strings[S.bonusBalance],
                    color = settings.ringBonusColor,
                    onColorChange = onRingBonusColorChange,
                )
                RingColorSetting(
                    label = strings[S.used],
                    color = settings.ringUsedColor,
                    onColorChange = onRingUsedColorChange,
                )
                RingColorSetting(
                    label = strings[S.todayUsage],
                    color = settings.ringTodayColor,
                    onColorChange = onRingTodayColorChange,
                )
                TextButton(onClick = onResetRingColors) {
                    Text(strings[S.resetColors])
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(strings[S.security], style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    if (state.isLoggedIn) strings[S.sessionLoggedIn] else strings[S.sessionLoggedOut],
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                HorizontalDivider()
                if (state.isLoggedIn) {
                    Button(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(strings[S.logout])
                    }
                } else {
                    Button(
                        onClick = onLogin,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(strings[S.loginDeepSeek])
                    }
                }
            }
        }

        Text(
            strings.format(S.thresholdNote, "0" to (state.dashboard?.primaryCurrency ?: "CNY")),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun RefreshIntervalSelector(
    selectedSeconds: Int,
    strings: com.deepseek.meter.ui.localization.AppStrings,
    onSelected: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(refreshLabel(selectedSeconds, strings))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            AppSettings.refreshOptions.forEach { seconds ->
                DropdownMenuItem(
                    text = { Text(refreshLabel(seconds, strings)) },
                    onClick = {
                        expanded = false
                        onSelected(seconds)
                    },
                )
            }
        }
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun ThresholdInput(
    label: String,
    value: Double,
    onValueChange: (Double) -> Unit,
) {
    var text by remember(value) { mutableStateOf(formatAmount(value)) }
    OutlinedTextField(
        value = text,
        onValueChange = { updated ->
            text = updated
            updated.toDoubleOrNull()?.let(onValueChange)
        },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    )
}

@Composable
private fun RingColorSetting(
    label: String,
    color: Long,
    onColorChange: (Long) -> Unit,
) {
    var hexText by remember(color) { mutableStateOf(colorToHex(color)) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(Color(color), CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
            )
            Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            OutlinedTextField(
                value = hexText,
                onValueChange = { updated ->
                    hexText = updated
                    parseHexColor(updated)?.let(onColorChange)
                },
                modifier = Modifier.width(128.dp),
                singleLine = true,
                label = { Text("HEX") },
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RING_COLOR_PRESETS.forEach { preset ->
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color(preset), CircleShape)
                        .border(
                            width = if (preset == color) 2.dp else 1.dp,
                            color = if (preset == color) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline,
                            shape = CircleShape,
                        )
                        .clickable { onColorChange(preset) },
                )
            }
        }
    }
}

private val RING_COLOR_PRESETS = listOf(
    0xFF0F766EL,
    0xFF2563EBL,
    0xFF7C3AEDL,
    0xFFB45309L,
    0xFFC2410CL,
    0xFFB91C1CL,
    0xFF4D7C0FL,
    0xFF334155L,
)

private fun colorToHex(value: Long): String =
    "#" + String.format(Locale.ROOT, "%06X", value and 0xFFFFFFL)

private fun parseHexColor(value: String): Long? {
    val hex = value.trim().removePrefix("#")
    if (hex.length != 6) return null
    val rgb = hex.toLongOrNull(16) ?: return null
    return 0xFF000000L or rgb
}

private fun refreshLabel(seconds: Int, strings: com.deepseek.meter.ui.localization.AppStrings): String = when (seconds) {
    0 -> strings[S.intervalOff]
    15, 30 -> strings.format(S.secondsShort, "0" to seconds)
    60, 300, 600 -> strings.format(S.minutesShort, "0" to seconds / 60)
    else -> strings.format(S.secondsShort, "0" to seconds)
}

@Composable
private fun LanguageSelector(selectedCode: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(languageName(selectedCode))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            supportedLanguages.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language.nativeName) },
                    onClick = {
                        expanded = false
                        onSelected(language.code)
                    },
                )
            }
        }
    }
}
