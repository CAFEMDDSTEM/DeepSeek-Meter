package com.deepseek.meter.data.model

import java.time.Instant
import java.time.LocalDate

data class MoneyAmount(
    val currency: String,
    val amount: Double,
)

data class WalletBalance(
    val currency: String,
    val balance: Double,
    val tokenEstimation: Long?,
)

data class AccountSummary(
    val totalUsage: Double?,
    val monthlyUsage: Double?,
    val totalAvailableTokenEstimation: Long?,
    val normalWallets: List<WalletBalance>,
    val bonusWallets: List<WalletBalance>,
    val totalCosts: List<MoneyAmount>,
)

data class UsageRecord(
    val apiKeyId: String,
    val apiKeyName: String?,
    val model: String,
    val timeEpochSecond: Long,
    val requestCount: Long,
    val promptCacheHitToken: Long,
    val promptCacheMissToken: Long,
    val responseToken: Long,
) {
    val inputToken: Long = promptCacheHitToken + promptCacheMissToken
    val totalToken: Long = inputToken + responseToken
}

data class CostRecord(
    val currency: String,
    val apiKeyId: String,
    val apiKeyName: String?,
    val model: String,
    val timeEpochSecond: Long,
    val cost: Double,
)

data class TokenStats(
    val cost: Double,
    val requests: Long,
    val inputTokens: Long,
    val outputTokens: Long,
    val cacheHitTokens: Long,
    val totalTokens: Long,
) {
    companion object {
        val Empty = TokenStats(0.0, 0, 0, 0, 0, 0)
    }
}

data class ModelUsage(
    val model: String,
    val requests: Long,
    val inputTokens: Long,
    val outputTokens: Long,
    val cacheHitTokens: Long,
    val totalTokens: Long,
    val costs: List<MoneyAmount>,
) {
    fun costFor(currency: String): Double = costs.firstOrNull { it.currency == currency }?.amount ?: 0.0
}

data class TrendPoint(
    val date: LocalDate,
    val requests: Long,
    val promptCacheMissTokens: Long,
    val promptCacheHitTokens: Long,
    val outputTokens: Long,
) {
    val inputTokens: Long = promptCacheMissTokens + promptCacheHitTokens
    val totalTokens: Long = inputTokens + outputTokens
}

data class CostTrendPoint(
    val date: LocalDate,
    val byApiKey: Map<String, Double>,
    val byModel: Map<String, Double>,
    val byApiKeyModel: Map<String, Double>,
) {
    val total: Double = byApiKey.values.sum()
}

data class CurrencyBalance(
    val currency: String,
    val rechargeRemaining: Double,
    val bonusRemaining: Double,
    val used: Double,
    val total: Double,
    val todayCost: Double,
    val monthCost: Double,
) {
    val remaining: Double = rechargeRemaining + bonusRemaining
}

data class DashboardSnapshot(
    val balances: List<CurrencyBalance>,
    val primaryCurrency: String,
    val today: TokenStats,
    val month: TokenStats,
    val models: List<ModelUsage>,
    val trend: List<TrendPoint>,
    val costTrend: List<CostTrendPoint>,
    val totalAvailableTokenEstimation: Long?,
    val lastUpdated: Instant,
) {
    val primaryBalance: CurrencyBalance?
        get() = balances.firstOrNull { it.currency == primaryCurrency } ?: balances.firstOrNull()
}

data class AppSettings(
    val languageCode: String = DEFAULT_LANGUAGE_CODE,
    val refreshIntervalSeconds: Int = DEFAULT_REFRESH_SECONDS,
    val todayAlertEnabled: Boolean = false,
    val todayAlertThreshold: Double = 5.0,
    val lowBalanceAlertEnabled: Boolean = false,
    val lowBalanceThreshold: Double = 10.0,
    val lastTodayAlertKey: String? = null,
    val lowBalanceActive: Boolean = false,
    val ringRechargeColor: Long = DEFAULT_RING_RECHARGE_COLOR,
    val ringBonusColor: Long = DEFAULT_RING_BONUS_COLOR,
    val ringUsedColor: Long = DEFAULT_RING_USED_COLOR,
    val ringTodayColor: Long = DEFAULT_RING_TODAY_COLOR,
) {
    companion object {
        const val DEFAULT_LANGUAGE_CODE = "zh"
        const val DEFAULT_REFRESH_SECONDS = 60
        const val DEFAULT_RING_RECHARGE_COLOR = 0xFF0F766EL
        const val DEFAULT_RING_BONUS_COLOR = 0xFF7C3AEDL
        const val DEFAULT_RING_USED_COLOR = 0xFFB45309L
        const val DEFAULT_RING_TODAY_COLOR = 0xFFC2410CL
        val refreshOptions = listOf(0, 15, 30, 60, 300, 600)
    }
}

enum class ServiceHealth {
    Operational,
    Degraded,
    PartialOutage,
    FullOutage,
    Maintenance,
    Unknown,
}

data class StatusComponent(
    val id: String,
    val name: String,
    val description: String,
    val sectionId: String?,
    val health: ServiceHealth,
    val uptimePercent: Double?,
)

data class StatusSection(
    val id: String,
    val name: String,
    val components: List<StatusComponent>,
)

data class StatusIncident(
    val id: String,
    val title: String,
    val status: String,
    val description: String,
    val startEpochSecond: Long?,
    val closeEpochSecond: Long?,
    val affectedComponents: List<String>,
)

data class DeepSeekServiceStatus(
    val overall: ServiceHealth,
    val sections: List<StatusSection>,
    val ungroupedComponents: List<StatusComponent>,
    val activeIncidents: List<StatusIncident>,
    val recentIncidents: List<StatusIncident>,
    val lastUpdated: Instant,
) {
    val isAbnormal: Boolean
        get() = overall != ServiceHealth.Operational && overall != ServiceHealth.Unknown
}

data class AppUpdateInfo(
    val latestVersion: String,
    val releaseUrl: String,
    val apkUrl: String?,
    val releaseNotes: String,
)

