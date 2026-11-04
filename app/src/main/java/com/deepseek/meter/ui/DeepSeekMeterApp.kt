package com.deepseek.meter.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deepseek.meter.ui.dashboard.OverviewScreen
import com.deepseek.meter.ui.localization.LocalAppStrings
import com.deepseek.meter.ui.localization.S
import com.deepseek.meter.ui.localization.stringsFor
import com.deepseek.meter.ui.login.LoginScreen
import com.deepseek.meter.ui.models.ModelsScreen
import com.deepseek.meter.ui.settings.SettingsScreen

@Composable
fun DeepSeekMeterApp(viewModel: MainViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val strings = stringsFor(state.settings.languageCode)

    CompositionLocalProvider(LocalAppStrings provides strings) {
    when {
        state.checkingSession -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.showLogin -> {
            BackHandler { viewModel.cancelLogin() }
            LoginScreen(
                authenticating = state.authenticating,
                errorMessage = state.errorMessage,
                onSessionToken = viewModel::onWebSessionToken,
                onCancel = viewModel::cancelLogin,
            )
        }

        else -> {
            MainScaffold(
                state = state,
                onRefresh = viewModel::refresh,
                onLogin = viewModel::openLogin,
                onLanguageChange = viewModel::setLanguageCode,
                onDismissError = viewModel::clearError,
                onRefreshIntervalChange = viewModel::setRefreshInterval,
                onTodayAlertEnabledChange = viewModel::setTodayAlertEnabled,
                onTodayAlertThresholdChange = viewModel::setTodayAlertThreshold,
                onLowBalanceAlertEnabledChange = viewModel::setLowBalanceAlertEnabled,
                onLowBalanceThresholdChange = viewModel::setLowBalanceThreshold,
                onRingRechargeColorChange = viewModel::setRingRechargeColor,
                onRingBonusColorChange = viewModel::setRingBonusColor,
                onRingUsedColorChange = viewModel::setRingUsedColor,
                onRingTodayColorChange = viewModel::setRingTodayColor,
                onResetRingColors = viewModel::resetRingColors,
                onLogout = viewModel::logout,
            )
        }
    }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScaffold(
    state: MainUiState,
    onRefresh: () -> Unit,
    onLogin: () -> Unit,
    onLanguageChange: (String) -> Unit,
    onDismissError: () -> Unit,
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
    onLogout: () -> Unit,
) {
    val strings = LocalAppStrings.current
    var sectionName by rememberSaveable { mutableStateOf(AppSection.Overview.name) }
    val section = AppSection.valueOf(sectionName)
    var showRecharge by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    var notificationPermissionGranted by remember {
        mutableStateOf(hasNotificationPermission(context))
    }
    var pendingTodayAlert by remember { mutableStateOf(false) }
    var pendingLowBalanceAlert by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        notificationPermissionGranted = granted
        if (granted) {
            if (pendingTodayAlert) onTodayAlertEnabledChange(true)
            if (pendingLowBalanceAlert) onLowBalanceAlertEnabledChange(true)
        }
        pendingTodayAlert = false
        pendingLowBalanceAlert = false
    }

    val requestPermission: (Boolean, Boolean) -> Unit = { today, lowBalance ->
        if (Build.VERSION.SDK_INT >= 33 && !notificationPermissionGranted) {
            pendingTodayAlert = today
            pendingLowBalanceAlert = lowBalance
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            if (today) onTodayAlertEnabledChange(true)
            if (lowBalance) onLowBalanceAlertEnabledChange(true)
        }
    }

    if (showRecharge) {
        BackHandler { showRecharge = false }
        RechargeOverlay(onClose = { showRecharge = false })
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(strings[S.appName], fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                actions = {
                    if (!state.isLoggedIn) {
                        TextButton(onClick = onLogin) { Text(strings[S.login]) }
                    }
                    if (state.refreshing) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .size(22.dp),
                            strokeWidth = 2.dp,
                        )
                    } else if (state.isLoggedIn) {
                        IconButton(onClick = onRefresh) {
                            Icon(Icons.Outlined.Refresh, contentDescription = strings[S.refresh])
                        }
                    }
                    IconButton(onClick = { sectionName = AppSection.Settings.name }) {
                        Icon(Icons.Outlined.Settings, contentDescription = strings[S.settings])
                    }
                },
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationItem(
                    selected = section == AppSection.Overview,
                    label = strings[S.overview],
                    icon = { Icon(Icons.Outlined.Dashboard, contentDescription = null) },
                    onClick = { sectionName = AppSection.Overview.name },
                )
                NavigationItem(
                    selected = section == AppSection.Models,
                    label = strings[S.models],
                    icon = { Icon(Icons.Outlined.BarChart, contentDescription = null) },
                    onClick = { sectionName = AppSection.Models.name },
                )
                NavigationItem(
                    selected = section == AppSection.Settings,
                    label = strings[S.settings],
                    icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                    onClick = { sectionName = AppSection.Settings.name },
                )
            }
        },
    ) { innerPadding ->
        when (section) {
            AppSection.Overview -> OverviewScreen(
                state = state,
                onRefresh = onRefresh,
                onRecharge = { showRecharge = true },
                onLogin = onLogin,
                onDismissError = onDismissError,
                modifier = Modifier.padding(innerPadding),
            )

            AppSection.Models -> ModelsScreen(
                state = state,
                onLogin = onLogin,
                modifier = Modifier.padding(innerPadding),
            )

            AppSection.Settings -> SettingsScreen(
                state = state,
                notificationPermissionGranted = notificationPermissionGranted,
                onRefreshIntervalChange = onRefreshIntervalChange,
                onTodayAlertEnabledChange = { enabled ->
                    if (enabled) requestPermission(true, false) else onTodayAlertEnabledChange(false)
                },
                onTodayAlertThresholdChange = onTodayAlertThresholdChange,
                onLowBalanceAlertEnabledChange = { enabled ->
                    if (enabled) requestPermission(false, true) else onLowBalanceAlertEnabledChange(false)
                },
                onLowBalanceThresholdChange = onLowBalanceThresholdChange,
                onRingRechargeColorChange = onRingRechargeColorChange,
                onRingBonusColorChange = onRingBonusColorChange,
                onRingUsedColorChange = onRingUsedColorChange,
                onRingTodayColorChange = onRingTodayColorChange,
                onResetRingColors = onResetRingColors,
                onLanguageChange = onLanguageChange,
                onLogin = onLogin,
                onLogout = onLogout,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
private fun RowScope.NavigationItem(
    selected: Boolean,
    label: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = icon,
        label = { Text(label) },
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun RechargeOverlay(onClose: () -> Unit) {
    val strings = LocalAppStrings.current
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(strings[S.officialRecharge], style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(strings[S.sameSession], style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    OutlinedButton(onClick = onClose) { Text(strings[S.close]) }
                }
                AndroidView(
                    modifier = Modifier.weight(1f),
                    factory = { context ->
                        WebView(context).apply {
                            WebView.setWebContentsDebuggingEnabled(false)
                            CookieManager.getInstance().setAcceptCookie(true)
                            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.allowFileAccess = false
                            settings.allowContentAccess = false
                            webViewClient = WebViewClient()
                            loadUrl("https://platform.deepseek.com/top_up")
                        }
                    },
                    onRelease = { it.destroy() },
                )
            }
        }
    }
}

private fun hasNotificationPermission(context: android.content.Context): Boolean =
    Build.VERSION.SDK_INT < 33 ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
