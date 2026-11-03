package com.deepseek.meter.data.store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.deepseek.meter.data.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.settingsDataStore by preferencesDataStore(name = "deepseek_meter_settings")

class SettingsStore(private val context: Context) {
    val settings: Flow<AppSettings> = context.settingsDataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferences ->
            AppSettings(
                languageCode = preferences[LANGUAGE_CODE] ?: AppSettings.DEFAULT_LANGUAGE_CODE,
                refreshIntervalSeconds = preferences[REFRESH_SECONDS] ?: AppSettings.DEFAULT_REFRESH_SECONDS,
                todayAlertEnabled = preferences[TODAY_ALERT_ENABLED] ?: false,
                todayAlertThreshold = preferences[TODAY_ALERT_THRESHOLD] ?: 5.0,
                lowBalanceAlertEnabled = preferences[LOW_BALANCE_ALERT_ENABLED] ?: false,
                lowBalanceThreshold = preferences[LOW_BALANCE_THRESHOLD] ?: 10.0,
                lastTodayAlertKey = preferences[LAST_TODAY_ALERT_KEY],
                lowBalanceActive = preferences[LOW_BALANCE_ACTIVE] ?: false,
                ringRechargeColor = preferences[RING_RECHARGE_COLOR] ?: AppSettings.DEFAULT_RING_RECHARGE_COLOR,
                ringBonusColor = preferences[RING_BONUS_COLOR] ?: AppSettings.DEFAULT_RING_BONUS_COLOR,
                ringUsedColor = preferences[RING_USED_COLOR] ?: AppSettings.DEFAULT_RING_USED_COLOR,
                ringTodayColor = preferences[RING_TODAY_COLOR] ?: AppSettings.DEFAULT_RING_TODAY_COLOR,
            )
        }

    suspend fun current(): AppSettings = settings.first()

    suspend fun setLanguageCode(code: String) {
        context.settingsDataStore.edit { it[LANGUAGE_CODE] = code }
    }

    suspend fun setRefreshInterval(seconds: Int) {
        context.settingsDataStore.edit { it[REFRESH_SECONDS] = seconds }
    }

    suspend fun setTodayAlertEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { it[TODAY_ALERT_ENABLED] = enabled }
    }

    suspend fun setTodayAlertThreshold(value: Double) {
        context.settingsDataStore.edit { it[TODAY_ALERT_THRESHOLD] = value.coerceAtLeast(0.0) }
    }

    suspend fun setLowBalanceAlertEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { it[LOW_BALANCE_ALERT_ENABLED] = enabled }
    }

    suspend fun setLowBalanceThreshold(value: Double) {
        context.settingsDataStore.edit { it[LOW_BALANCE_THRESHOLD] = value.coerceAtLeast(0.0) }
    }

    suspend fun setLastTodayAlertKey(key: String) {
        context.settingsDataStore.edit { it[LAST_TODAY_ALERT_KEY] = key }
    }

    suspend fun setLowBalanceActive(active: Boolean) {
        context.settingsDataStore.edit { it[LOW_BALANCE_ACTIVE] = active }
    }

    suspend fun setRingRechargeColor(color: Long) {
        context.settingsDataStore.edit { it[RING_RECHARGE_COLOR] = color }
    }

    suspend fun setRingBonusColor(color: Long) {
        context.settingsDataStore.edit { it[RING_BONUS_COLOR] = color }
    }

    suspend fun setRingUsedColor(color: Long) {
        context.settingsDataStore.edit { it[RING_USED_COLOR] = color }
    }

    suspend fun setRingTodayColor(color: Long) {
        context.settingsDataStore.edit { it[RING_TODAY_COLOR] = color }
    }

    suspend fun resetRingColors() {
        context.settingsDataStore.edit { preferences ->
            preferences[RING_RECHARGE_COLOR] = AppSettings.DEFAULT_RING_RECHARGE_COLOR
            preferences[RING_BONUS_COLOR] = AppSettings.DEFAULT_RING_BONUS_COLOR
            preferences[RING_USED_COLOR] = AppSettings.DEFAULT_RING_USED_COLOR
            preferences[RING_TODAY_COLOR] = AppSettings.DEFAULT_RING_TODAY_COLOR
        }
    }

    private companion object {
        val LANGUAGE_CODE = stringPreferencesKey("language_code")
        val REFRESH_SECONDS = intPreferencesKey("refresh_seconds")
        val TODAY_ALERT_ENABLED = booleanPreferencesKey("today_alert_enabled")
        val TODAY_ALERT_THRESHOLD = doublePreferencesKey("today_alert_threshold")
        val LOW_BALANCE_ALERT_ENABLED = booleanPreferencesKey("low_balance_alert_enabled")
        val LOW_BALANCE_THRESHOLD = doublePreferencesKey("low_balance_threshold")
        val LAST_TODAY_ALERT_KEY = stringPreferencesKey("last_today_alert_key")
        val LOW_BALANCE_ACTIVE = booleanPreferencesKey("low_balance_active")
        val RING_RECHARGE_COLOR = longPreferencesKey("ring_recharge_color")
        val RING_BONUS_COLOR = longPreferencesKey("ring_bonus_color")
        val RING_USED_COLOR = longPreferencesKey("ring_used_color")
        val RING_TODAY_COLOR = longPreferencesKey("ring_today_color")
    }
}
