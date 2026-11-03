package com.deepseek.meter.data.repository

import com.deepseek.meter.data.api.DeepSeekApi
import com.deepseek.meter.data.model.AccountSummary
import com.deepseek.meter.data.model.CostRecord
import com.deepseek.meter.data.model.CostTrendPoint
import com.deepseek.meter.data.model.CurrencyBalance
import com.deepseek.meter.data.model.DashboardSnapshot
import com.deepseek.meter.data.model.ModelUsage
import com.deepseek.meter.data.model.MoneyAmount
import com.deepseek.meter.data.model.TokenStats
import com.deepseek.meter.data.model.TrendPoint
import com.deepseek.meter.data.model.UsageRecord
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class DashboardRepository(
    private val api: DeepSeekApi = DeepSeekApi(),
) {
    suspend fun loadDashboard(token: String): DashboardSnapshot = coroutineScope {
        val zoneId = ZoneId.systemDefault()
        val today = LocalDate.now(zoneId)
        val monthStart = today.withDayOfMonth(1)
        val startEpochSecond = monthStart.atStartOfDay(zoneId).toEpochSecond()
        val endEpochSecond = today.plusDays(1).atStartOfDay(zoneId).toEpochSecond()
        val timezoneOffset = zoneId.rules.getOffset(Instant.now()).totalSeconds

        val summary = async { api.fetchAccountSummary(token) }
        val usage = async { api.fetchUsage(token, startEpochSecond, endEpochSecond, timezoneOffset) }
        val costs = async { api.fetchCosts(token, startEpochSecond, endEpochSecond, timezoneOffset) }

        buildSnapshot(
            summary = summary.await(),
            usageRecords = usage.await(),
            costRecords = costs.await(),
            zoneId = zoneId,
            today = today,
        )
    }

    private fun buildSnapshot(
        summary: AccountSummary,
        usageRecords: List<UsageRecord>,
        costRecords: List<CostRecord>,
        zoneId: ZoneId,
        today: LocalDate,
    ): DashboardSnapshot {
        val rechargeByCurrency = mutableMapOf<String, Double>()
        summary.normalWallets.forEach { wallet ->
            rechargeByCurrency[wallet.currency] = rechargeByCurrency.getOrDefault(wallet.currency, 0.0) + wallet.balance
        }
        val bonusByCurrency = mutableMapOf<String, Double>()
        summary.bonusWallets.forEach { wallet ->
            bonusByCurrency[wallet.currency] = bonusByCurrency.getOrDefault(wallet.currency, 0.0) + wallet.balance
        }

        val usedByCurrency = mutableMapOf<String, Double>()
        summary.totalCosts.forEach { cost ->
            usedByCurrency[cost.currency] = usedByCurrency.getOrDefault(cost.currency, 0.0) + cost.amount
        }

        val currencies = buildList {
            addAll(rechargeByCurrency.keys)
            addAll(bonusByCurrency.keys)
            addAll(usedByCurrency.keys)
            addAll(costRecords.map { it.currency })
        }.distinct()
        val primaryCurrency = choosePrimaryCurrency(currencies)

        val balances = currencies.map { currency ->
            val rechargeRemaining = rechargeByCurrency.getOrDefault(currency, 0.0)
            val bonusRemaining = bonusByCurrency.getOrDefault(currency, 0.0)
            val used = usedByCurrency.getOrDefault(currency, 0.0)
            CurrencyBalance(
                currency = currency,
                rechargeRemaining = rechargeRemaining,
                bonusRemaining = bonusRemaining,
                used = used,
                total = rechargeRemaining + bonusRemaining + used,
                todayCost = costRecords
                    .filter { it.currency == currency && it.localDate(zoneId) == today }
                    .sumOf { it.cost },
                monthCost = costRecords
                    .filter { it.currency == currency }
                    .sumOf { it.cost },
            )
        }.sortedWith(compareByDescending<CurrencyBalance> { it.total }.thenBy { it.currency })

        val todayStats = usageRecords
            .filter { it.localDate(zoneId) == today }
            .toTokenStats(
                cost = costRecords
                    .filter { it.currency == primaryCurrency && it.localDate(zoneId) == today }
                    .sumOf { it.cost },
            )

        val monthStats = usageRecords.toTokenStats(
            cost = costRecords
                .filter { it.currency == primaryCurrency }
                .sumOf { it.cost },
        )

        return DashboardSnapshot(
            balances = balances,
            primaryCurrency = primaryCurrency,
            today = todayStats,
            month = monthStats,
            models = buildModels(usageRecords, costRecords, primaryCurrency),
            trend = buildTrend(usageRecords, zoneId, today),
            costTrend = buildCostTrend(costRecords, primaryCurrency, zoneId, today),
            totalAvailableTokenEstimation = summary.totalAvailableTokenEstimation,
            lastUpdated = Instant.now(),
        )
    }

    private fun choosePrimaryCurrency(currencies: List<String>): String =
        when {
            "CNY" in currencies -> "CNY"
            "USD" in currencies -> "USD"
            currencies.isNotEmpty() -> currencies.first()
            else -> "CNY"
        }

    private fun buildModels(
        usageRecords: List<UsageRecord>,
        costRecords: List<CostRecord>,
        primaryCurrency: String,
    ): List<ModelUsage> {
        data class MutableModel(
            var requests: Long = 0,
            var input: Long = 0,
            var output: Long = 0,
            var cacheHit: Long = 0,
            val costs: MutableMap<String, Double> = mutableMapOf(),
        )

        val models = mutableMapOf<String, MutableModel>()
        usageRecords.forEach { record ->
            val model = models.getOrPut(record.model) { MutableModel() }
            model.requests += record.requestCount
            model.input += record.inputToken
            model.output += record.responseToken
            model.cacheHit += record.promptCacheHitToken
        }
        costRecords.forEach { record ->
            val model = models.getOrPut(record.model) { MutableModel() }
            model.costs[record.currency] = model.costs.getOrDefault(record.currency, 0.0) + record.cost
        }

        return models.map { (name, value) ->
            ModelUsage(
                model = name,
                requests = value.requests,
                inputTokens = value.input,
                outputTokens = value.output,
                cacheHitTokens = value.cacheHit,
                totalTokens = value.input + value.output,
                costs = value.costs.map { MoneyAmount(it.key, it.value) }.sortedBy { it.currency },
            )
        }.sortedWith(
            compareByDescending<ModelUsage> { it.costFor(primaryCurrency) }
                .thenByDescending { it.totalTokens },
        )
    }

    private fun buildCostTrend(
        costRecords: List<CostRecord>,
        currency: String,
        zoneId: ZoneId,
        today: LocalDate,
    ): List<CostTrendPoint> {
        data class MutableCostTrend(
            val byApiKey: MutableMap<String, Double> = mutableMapOf(),
            val byModel: MutableMap<String, Double> = mutableMapOf(),
            val byApiKeyModel: MutableMap<String, Double> = mutableMapOf(),
        )

        val values = mutableMapOf<LocalDate, MutableCostTrend>()
        costRecords
            .filter { it.currency == currency }
            .forEach { record ->
                val date = record.localDate(zoneId)
                val bucket = values.getOrPut(date) { MutableCostTrend() }
                val keyLabel = record.apiKeyName?.takeIf { it.isNotBlank() }
                    ?: record.apiKeyId.takeIf { it.isNotBlank() }
                    ?: "untitled"
                bucket.byApiKey[keyLabel] = bucket.byApiKey.getOrDefault(keyLabel, 0.0) + record.cost
                bucket.byModel[record.model] = bucket.byModel.getOrDefault(record.model, 0.0) + record.cost
                val combined = keyLabel + " / " + record.model
                bucket.byApiKeyModel[combined] = bucket.byApiKeyModel.getOrDefault(combined, 0.0) + record.cost
            }

        return (1..today.lengthOfMonth()).map { day ->
            val date = today.withDayOfMonth(day)
            val bucket = values[date] ?: MutableCostTrend()
            CostTrendPoint(
                date = date,
                byApiKey = bucket.byApiKey.toMap(),
                byModel = bucket.byModel.toMap(),
                byApiKeyModel = bucket.byApiKeyModel.toMap(),
            )
        }
    }

    private fun buildTrend(
        usageRecords: List<UsageRecord>,
        zoneId: ZoneId,
        today: LocalDate,
    ): List<TrendPoint> {
        data class MutableTrend(
            var requests: Long = 0,
            var cacheMiss: Long = 0,
            var cacheHit: Long = 0,
            var output: Long = 0,
        )

        val values = mutableMapOf<LocalDate, MutableTrend>()
        usageRecords.forEach { record ->
            val date = record.localDate(zoneId)
            val value = values.getOrPut(date) { MutableTrend() }
            value.requests += record.requestCount
            value.cacheMiss += record.promptCacheMissToken
            value.cacheHit += record.promptCacheHitToken
            value.output += record.responseToken
        }

        return (1..today.lengthOfMonth()).map { day ->
            val date = today.withDayOfMonth(day)
            val value = values[date] ?: MutableTrend()
            TrendPoint(
                date = date,
                requests = value.requests,
                promptCacheMissTokens = value.cacheMiss,
                promptCacheHitTokens = value.cacheHit,
                outputTokens = value.output,
            )
        }
    }

    private fun List<UsageRecord>.toTokenStats(cost: Double): TokenStats = TokenStats(
        cost = cost,
        requests = sumOf { it.requestCount },
        inputTokens = sumOf { it.inputToken },
        outputTokens = sumOf { it.responseToken },
        cacheHitTokens = sumOf { it.promptCacheHitToken },
        totalTokens = sumOf { it.totalToken },
    )

    private fun UsageRecord.localDate(zoneId: ZoneId): LocalDate =
        Instant.ofEpochSecond(timeEpochSecond).atZone(zoneId).toLocalDate()

    private fun CostRecord.localDate(zoneId: ZoneId): LocalDate =
        Instant.ofEpochSecond(timeEpochSecond).atZone(zoneId).toLocalDate()
}
