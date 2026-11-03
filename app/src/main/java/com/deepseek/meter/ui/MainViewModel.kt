package com.deepseek.meter.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.deepseek.meter.data.api.ApiException
import com.deepseek.meter.data.model.AppSettings
import com.deepseek.meter.data.model.DashboardSnapshot
import com.deepseek.meter.data.repository.DashboardRepository
import com.deepseek.meter.data.store.SecureTokenStore
import com.deepseek.meter.data.store.SettingsStore
import com.deepseek.meter.notification.NotificationCoordinator
import com.deepseek.meter.ui.localization.S
import com.deepseek.meter.ui.localization.stringsFor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.time.Instant

enum class AppSection {
    Overview,
    Models,
    Settings,
}

data class MainUiState(
    val checkingSession: Boolean = true,
    val isLoggedIn: Boolean = false,
    val showLogin: Boolean = false,
    val authenticating: Boolean = false,
    val refreshing: Boolean = false,
    val dashboard: DashboardSnapshot? = null,
    val settings: AppSettings = AppSettings(),
    val errorMessage: String? = null,
    val lastUpdated: Instant? = null,
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val tokenStore = SecureTokenStore(application)
    private val settingsStore = SettingsStore(application)
    private val repository = DashboardRepository()
    private val notificationCoordinator = NotificationCoordinator(application, settingsStore)

    private val mutableUiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = mutableUiState.asStateFlow()

    private var activeSessionToken: String? = null
    private var rejectedTokenHash: String? = null
    private var refreshLoop: Job? = null

    init {
        observeSession()
        observeSettings()
    }

    fun onWebSessionToken(rawToken: String) {
        if (mutableUiState.value.authenticating) return
        val token = normalizeToken(rawToken) ?: run {
            mutableUiState.value = mutableUiState.value.copy(errorMessage = stringsFor(mutableUiState.value.settings.languageCode)[S.errorSessionRead])
            return
        }

        val tokenHash = token.sha256()
        if (tokenHash == rejectedTokenHash) return

        viewModelScope.launch {
            mutableUiState.value = mutableUiState.value.copy(authenticating = true, errorMessage = null)
            try {
                val snapshot = repository.loadDashboard(token)
                activeSessionToken = token
                rejectedTokenHash = null
                tokenStore.saveToken(token)
                applySuccessfulSnapshot(snapshot)
                notificationCoordinator.evaluate(snapshot, settingsStore.current())
            } catch (error: ApiException) {
                if (error.code == 40002 || error.code == 40003) {
                    rejectedTokenHash = tokenHash
                }
                mutableUiState.value = mutableUiState.value.copy(
                    authenticating = false,
                    errorMessage = if (error.code == 40002 || error.code == 40003) {
                        null
                    } else {
                        stringsFor(mutableUiState.value.settings.languageCode).format(S.errorLoginValidation, "0" to error.message)
                    },
                )
            } catch (_: Exception) {
                mutableUiState.value = mutableUiState.value.copy(
                    authenticating = false,
                    errorMessage = stringsFor(mutableUiState.value.settings.languageCode)[S.errorLoginNetwork],
                )
            }
        }
    }

    fun openLogin() {
        mutableUiState.value = mutableUiState.value.copy(
            showLogin = true,
            errorMessage = null,
        )
    }

    fun cancelLogin() {
        mutableUiState.value = mutableUiState.value.copy(showLogin = false)
    }

    fun refresh() {
        val token = activeSessionToken ?: return
        refreshInternal(token)
    }

    fun logout() {
        activeSessionToken = null
        rejectedTokenHash = null
        viewModelScope.launch {
            tokenStore.clearToken()
            mutableUiState.value = mutableUiState.value.copy(
                isLoggedIn = false,
                showLogin = false,
                dashboard = null,
                refreshing = false,
                authenticating = false,
                errorMessage = null,
                lastUpdated = null,
            )
        }
    }

    fun clearError() {
        mutableUiState.value = mutableUiState.value.copy(errorMessage = null)
    }

    fun setLanguageCode(code: String) {
        viewModelScope.launch { settingsStore.setLanguageCode(code) }
    }

    fun setRefreshInterval(seconds: Int) {
        viewModelScope.launch { settingsStore.setRefreshInterval(seconds) }
    }

    fun setTodayAlertEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsStore.setTodayAlertEnabled(enabled) }
    }

    fun setTodayAlertThreshold(value: Double) {
        viewModelScope.launch { settingsStore.setTodayAlertThreshold(value) }
    }

    fun setLowBalanceAlertEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsStore.setLowBalanceAlertEnabled(enabled) }
    }

    fun setLowBalanceThreshold(value: Double) {
        viewModelScope.launch { settingsStore.setLowBalanceThreshold(value) }
    }

    fun setRingRechargeColor(color: Long) {
        viewModelScope.launch { settingsStore.setRingRechargeColor(color) }
    }

    fun setRingBonusColor(color: Long) {
        viewModelScope.launch { settingsStore.setRingBonusColor(color) }
    }

    fun setRingUsedColor(color: Long) {
        viewModelScope.launch { settingsStore.setRingUsedColor(color) }
    }

    fun setRingTodayColor(color: Long) {
        viewModelScope.launch { settingsStore.setRingTodayColor(color) }
    }

    fun resetRingColors() {
        viewModelScope.launch { settingsStore.resetRingColors() }
    }

    private fun observeSession() {
        viewModelScope.launch {
            tokenStore.token.distinctUntilChanged().collectLatest { token ->
                if (token == activeSessionToken) {
                    mutableUiState.value = mutableUiState.value.copy(checkingSession = false)
                    return@collectLatest
                }
                activeSessionToken = token
                mutableUiState.value = mutableUiState.value.copy(checkingSession = false)
                if (token.isNullOrBlank()) {
                    mutableUiState.value = mutableUiState.value.copy(
                        isLoggedIn = false,
                        dashboard = null,
                        refreshing = false,
                    )
                } else {
                    mutableUiState.value = mutableUiState.value.copy(isLoggedIn = true)
                    refreshInternal(token)
                }
            }
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsStore.settings.collectLatest { settings ->
                mutableUiState.value = mutableUiState.value.copy(settings = settings)
                refreshLoop?.cancel()
                if (settings.refreshIntervalSeconds > 0) {
                    refreshLoop = viewModelScope.launch {
                        while (isActive) {
                            delay(settings.refreshIntervalSeconds * 1_000L)
                            if (mutableUiState.value.isLoggedIn) refresh()
                        }
                    }
                }
            }
        }
    }

    private fun refreshInternal(token: String) {
        if (mutableUiState.value.refreshing) return
        viewModelScope.launch {
            mutableUiState.value = mutableUiState.value.copy(refreshing = true)
            try {
                val snapshot = repository.loadDashboard(token)
                applySuccessfulSnapshot(snapshot)
                notificationCoordinator.evaluate(snapshot, settingsStore.current())
            } catch (error: ApiException) {
                if (error.code == 40002 || error.code == 40003) {
                    activeSessionToken = null
                    tokenStore.clearToken()
                    mutableUiState.value = mutableUiState.value.copy(
                        isLoggedIn = false,
                        showLogin = false,
                        refreshing = false,
                        authenticating = false,
                        dashboard = null,
                        errorMessage = stringsFor(mutableUiState.value.settings.languageCode)[S.errorSessionExpired],
                    )
                } else {
                    mutableUiState.value = mutableUiState.value.copy(
                        refreshing = false,
                        errorMessage = stringsFor(mutableUiState.value.settings.languageCode).format(S.errorRefresh, "0" to error.message),
                    )
                }
            } catch (_: Exception) {
                mutableUiState.value = mutableUiState.value.copy(
                    refreshing = false,
                    errorMessage = stringsFor(mutableUiState.value.settings.languageCode)[S.errorRefreshNetwork],
                )
            }
        }
    }

    private fun applySuccessfulSnapshot(snapshot: DashboardSnapshot) {
        mutableUiState.value = mutableUiState.value.copy(
            isLoggedIn = true,
            showLogin = false,
            authenticating = false,
            refreshing = false,
            dashboard = snapshot,
            errorMessage = null,
            lastUpdated = snapshot.lastUpdated,
        )
    }

    private fun normalizeToken(value: String): String? {
        val trimmed = value.trim().trim('"')
        return trimmed.takeIf { it.length >= 20 && it != "null" }
    }

    private fun String.sha256(): String =
        MessageDigest.getInstance("SHA-256")
            .digest(toByteArray(Charsets.UTF_8))
            .joinToString(separator = "") { byte -> "%02x".format(byte) }
}
