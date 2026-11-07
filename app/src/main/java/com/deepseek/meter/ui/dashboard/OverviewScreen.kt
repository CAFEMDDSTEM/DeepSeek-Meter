package com.deepseek.meter.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deepseek.meter.data.model.AppSettings
import com.deepseek.meter.data.model.CurrencyBalance
import com.deepseek.meter.data.model.DashboardSnapshot
import com.deepseek.meter.data.model.TokenStats
import com.deepseek.meter.ui.localization.LocalAppStrings
import com.deepseek.meter.ui.localization.S
import com.deepseek.meter.ui.MainUiState
import com.deepseek.meter.util.formatCount
import com.deepseek.meter.util.formatMoney
import com.deepseek.meter.util.formatTime
import com.deepseek.meter.util.formatToken

@Composable
fun OverviewScreen(
    state: MainUiState,
    onRefresh: () -> Unit,
    onRecharge: () -> Unit,
    onLogin: () -> Unit,
    onOpenStatus: () -> Unit,
    onDismissStatusBanner: () -> Unit,
    onDismissError: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppStrings.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        if (state.serviceStatus?.isAbnormal == true && !state.statusBannerDismissed) {
            StatusAlertBanner(
                statusLabel = statusLabel(state),
                onViewStatus = onOpenStatus,
                onDismiss = onDismissStatusBanner,
            )
        }
        if (state.errorMessage != null) {
            ErrorCard(
                message = state.errorMessage,
                onRetry = if (state.isLoggedIn) onRefresh else onLogin,
                retryLabel = if (state.isLoggedIn) strings[S.retry] else strings[S.login],
                onDismiss = onDismissError,
            )
        }

        val snapshot = state.dashboard
        if (snapshot == null) {
            if (state.isLoggedIn) LoadingCard() else LoginRequiredCard(onLogin = onLogin)
        } else {
            BalanceHeroCard(snapshot = snapshot, settings = state.settings, onRecharge = onRecharge, onRefresh = onRefresh, refreshing = state.refreshing)
            StatsCard(
                title = strings[S.todayStats],
                stats = snapshot.today,
                currency = snapshot.primaryCurrency,
            )
            StatsCard(
                title = strings[S.monthStats],
                stats = snapshot.month,
                currency = snapshot.primaryCurrency,
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(strings[S.tokenTrend], style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(
                        strings[S.tokenTrendDesc],
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    TokenTrendChart(points = snapshot.trend, modifier = Modifier.fillMaxWidth())
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(strings[S.costTrend], style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(
                        strings[S.costTrendDesc],
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    CostTrendChart(
                        points = snapshot.costTrend,
                        currency = snapshot.primaryCurrency,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            Text(
                strings.format(S.lastUpdated, "0" to (state.lastUpdated?.let(::formatTime) ?: "--")),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.End),
            )
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
private fun StatusAlertBanner(statusLabel: String, onViewStatus: () -> Unit, onDismiss: () -> Unit) {
    val strings = LocalAppStrings.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                strings[S.statusBannerTitle],
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onErrorContainer,
            )
            Text(
                strings.format(S.statusBannerText, "0" to statusLabel),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onViewStatus) { Text(strings[S.viewStatus]) }
                TextButton(onClick = onDismiss) { Text(strings[S.dismiss]) }
            }
        }
    }
}

@Composable
private fun statusLabel(state: MainUiState): String {
    val strings = LocalAppStrings.current
    return when (state.serviceStatus?.overall) {
        com.deepseek.meter.data.model.ServiceHealth.Degraded -> strings[S.degraded]
        com.deepseek.meter.data.model.ServiceHealth.PartialOutage -> strings[S.partialOutage]
        com.deepseek.meter.data.model.ServiceHealth.FullOutage -> strings[S.fullOutage]
        com.deepseek.meter.data.model.ServiceHealth.Maintenance -> strings[S.maintenance]
        else -> strings[S.statusUnknown]
    }
}

@Composable
private fun BalanceHeroCard(
    snapshot: DashboardSnapshot,
    settings: AppSettings,
    onRecharge: () -> Unit,
    onRefresh: () -> Unit,
    refreshing: Boolean,
) {
    val strings = LocalAppStrings.current
    val balance = snapshot.primaryBalance ?: CurrencyBalance(snapshot.primaryCurrency, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(strings[S.accountBalance], style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(
                        strings[S.quotaFormula],
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(snapshot.primaryCurrency, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            }

            BoxWithConstraints(Modifier.fillMaxWidth()) {
                if (maxWidth >= 560.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(26.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        BalanceRing(
                            rechargeRemaining = balance.rechargeRemaining,
                            bonusRemaining = balance.bonusRemaining,
                            used = balance.used,
                            today = balance.todayCost,
                            currency = balance.currency,
                            rechargeColor = Color(settings.ringRechargeColor),
                            bonusColor = Color(settings.ringBonusColor),
                            usedColor = Color(settings.ringUsedColor),
                            todayColor = Color(settings.ringTodayColor),
                        )
                        BalanceLegend(
                            rechargeRemaining = balance.rechargeRemaining,
                            bonusRemaining = balance.bonusRemaining,
                            used = balance.used,
                            today = balance.todayCost,
                            currency = balance.currency,
                            rechargeColor = Color(settings.ringRechargeColor),
                            bonusColor = Color(settings.ringBonusColor),
                            usedColor = Color(settings.ringUsedColor),
                            todayColor = Color(settings.ringTodayColor),
                            modifier = Modifier.weight(1f),
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        BalanceRing(
                            rechargeRemaining = balance.rechargeRemaining,
                            bonusRemaining = balance.bonusRemaining,
                            used = balance.used,
                            today = balance.todayCost,
                            currency = balance.currency,
                            rechargeColor = Color(settings.ringRechargeColor),
                            bonusColor = Color(settings.ringBonusColor),
                            usedColor = Color(settings.ringUsedColor),
                            todayColor = Color(settings.ringTodayColor),
                        )
                        BalanceLegend(
                            rechargeRemaining = balance.rechargeRemaining,
                            bonusRemaining = balance.bonusRemaining,
                            used = balance.used,
                            today = balance.todayCost,
                            currency = balance.currency,
                            rechargeColor = Color(settings.ringRechargeColor),
                            bonusColor = Color(settings.ringBonusColor),
                            usedColor = Color(settings.ringUsedColor),
                            todayColor = Color(settings.ringTodayColor),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }

            if (snapshot.balances.size > 1) {
                HorizontalDivider()
                snapshot.balances.filter { it.currency != snapshot.primaryCurrency }.forEach { other ->
                    Text(
                        strings.format(
                            S.otherCurrencySummary,
                            "currency" to other.currency,
                            "remaining" to formatMoney(other.remaining, other.currency),
                            "recharge" to formatMoney(other.rechargeRemaining, other.currency),
                            "bonus" to formatMoney(other.bonusRemaining, other.currency),
                            "used" to formatMoney(other.used, other.currency),
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(onClick = onRecharge) { Text(strings[S.officialRecharge]) }
                OutlinedButton(onClick = onRefresh, enabled = !refreshing) {
                    Icon(Icons.Outlined.Refresh, contentDescription = null)
                    Spacer(Modifier.padding(horizontal = 4.dp))
                    Text(if (refreshing) strings[S.refreshing] else strings[S.refresh])
                }
            }
        }
    }
}

@Composable
private fun StatsCard(title: String, stats: TokenStats, currency: String) {
    val strings = LocalAppStrings.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            StatRow(strings[S.cost], formatMoney(stats.cost, currency), strings[S.requests], formatCount(stats.requests))
            StatRow(strings[S.input], formatToken(stats.inputTokens), strings[S.output], formatToken(stats.outputTokens))
            StatRow(strings[S.cacheHit], formatToken(stats.cacheHitTokens), strings[S.totalTokens], formatToken(stats.totalTokens))
        }
    }
}

@Composable
private fun StatRow(leftLabel: String, leftValue: String, rightLabel: String, rightValue: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        MetricItem(leftLabel, leftValue, Modifier.weight(1f))
        MetricItem(rightLabel, rightValue, Modifier.weight(1f))
    }
}

@Composable
private fun MetricItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun ErrorCard(
    message: String,
    onRetry: () -> Unit,
    retryLabel: String,
    onDismiss: () -> Unit,
) {
    val strings = LocalAppStrings.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(message, color = MaterialTheme.colorScheme.onErrorContainer)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onRetry) { Text(retryLabel) }
                TextButton(onClick = onDismiss) { Text(strings[S.close]) }
            }
        }
    }
}


@Composable
private fun LoginRequiredCard(onLogin: () -> Unit) {
    val strings = LocalAppStrings.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(24.dp),
            ) {
                Text(strings[S.notLoggedIn], style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Text(
                    strings[S.loginDesc],
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Button(onClick = onLogin) { Text(strings[S.loginDeepSeek]) }
            }
        }
    }
}

@Composable
private fun LoadingCard() {
    val strings = LocalAppStrings.current
    Card(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CircularProgressIndicator()
                Text(strings[S.loadingData])
            }
        }
    }
}
