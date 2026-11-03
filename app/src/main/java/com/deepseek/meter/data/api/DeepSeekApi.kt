package com.deepseek.meter.data.api

import com.deepseek.meter.data.model.AccountSummary
import com.deepseek.meter.data.model.CostRecord
import com.deepseek.meter.data.model.MoneyAmount
import com.deepseek.meter.data.model.UsageRecord
import com.deepseek.meter.data.model.WalletBalance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class ApiException(
    val code: Int,
    message: String,
) : IOException(message)

class DeepSeekApi(
    private val client: OkHttpClient = defaultClient(),
    private val baseUrl: String = "https://platform.deepseek.com",
) {
    suspend fun fetchAccountSummary(token: String): AccountSummary =
        parseSummary(getJson("/api/v0/users/get_user_summary", token))

    suspend fun fetchUsage(
        token: String,
        startEpochSecond: Long,
        endEpochSecond: Long,
        timezoneOffsetSeconds: Int,
    ): List<UsageRecord> =
        parseUsage(
            getJson(
                path = "/api/v0/usage/by_api_key/amount",
                token = token,
                query = mapOf(
                    "start" to startEpochSecond.toString(),
                    "end" to endEpochSecond.toString(),
                    "tz" to timezoneOffsetSeconds.toString(),
                ),
            ),
        )

    suspend fun fetchCosts(
        token: String,
        startEpochSecond: Long,
        endEpochSecond: Long,
        timezoneOffsetSeconds: Int,
    ): List<CostRecord> =
        parseCosts(
            getJson(
                path = "/api/v0/usage/by_api_key/cost",
                token = token,
                query = mapOf(
                    "start" to startEpochSecond.toString(),
                    "end" to endEpochSecond.toString(),
                    "tz" to timezoneOffsetSeconds.toString(),
                ),
            ),
        )

    private suspend fun getJson(
        path: String,
        token: String,
        query: Map<String, String> = emptyMap(),
    ): JSONObject = withContext(Dispatchers.IO) {
        val urlBuilder = (baseUrl + path).toHttpUrl().newBuilder()
        query.forEach { (key, value) -> urlBuilder.addQueryParameter(key, value) }
        val request = Request.Builder()
            .url(urlBuilder.build())
            .header("Authorization", "Bearer $token")
            .header("Accept", "application/json")
            .header("Accept-Language", "zh-CN,zh;q=0.9")
            .header("User-Agent", "DeepSeekMeter/1.0 Android")
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()
            if (!response.isSuccessful) {
                throw ApiException(-response.code, "Server returned HTTP ${response.code}")
            }
            val json = try {
                JSONObject(body)
            } catch (_: Exception) {
                throw ApiException(-1, "The server returned unreadable data")
            }
            val code = json.intValue("code", 0)
            if (code != 0) {
                throw ApiException(code, json.stringValue("msg") ?: "API request failed")
            }
            json
        }
    }

    private fun parseSummary(json: JSONObject): AccountSummary {
        val data = json.bizData()
        return AccountSummary(
            totalUsage = data.doubleValue("total_usage"),
            monthlyUsage = data.doubleValue("monthly_usage"),
            totalAvailableTokenEstimation = data.longValue("total_available_token_estimation"),
            normalWallets = data.optJSONArray("normal_wallets").mapWallets(),
            bonusWallets = data.optJSONArray("bonus_wallets").mapWallets(),
            totalCosts = data.optJSONArray("total_costs").map { item ->
                MoneyAmount(
                    currency = item.stringValue("currency") ?: "CNY",
                    amount = item.doubleValue("amount") ?: 0.0,
                )
            },
        )
    }

    private fun parseUsage(json: JSONObject): List<UsageRecord> {
        val records = mutableListOf<UsageRecord>()
        val series = json.bizData().optJSONArray("series") ?: JSONArray()
        for (index in 0 until series.length()) {
            val item = series.optJSONObject(index) ?: continue
            val apiKey = item.optJSONObject("api_key")
            val apiKeyId = apiKey?.stringValue("tracking_id") ?: "untracked"
            val apiKeyName = apiKey?.stringValue("name")
            val model = item.stringValue("model") ?: "unknown"
            val buckets = item.optJSONArray("buckets") ?: JSONArray()
            for (bucketIndex in 0 until buckets.length()) {
                val bucket = buckets.optJSONObject(bucketIndex) ?: continue
                val usage = bucket.optJSONObject("usage") ?: continue
                records += UsageRecord(
                    apiKeyId = apiKeyId,
                    apiKeyName = apiKeyName,
                    model = model,
                    timeEpochSecond = bucket.longValue("time") ?: continue,
                    requestCount = usage.longValue("REQUEST") ?: 0,
                    promptCacheHitToken = usage.longValue("PROMPT_CACHE_HIT_TOKEN") ?: 0,
                    promptCacheMissToken = usage.longValue("PROMPT_CACHE_MISS_TOKEN") ?: 0,
                    responseToken = usage.longValue("RESPONSE_TOKEN") ?: 0,
                )
            }
        }
        return records
    }

    private fun parseCosts(json: JSONObject): List<CostRecord> {
        val records = mutableListOf<CostRecord>()
        val currencies = json.bizData().optJSONArray("data") ?: JSONArray()
        for (currencyIndex in 0 until currencies.length()) {
            val currencyBlock = currencies.optJSONObject(currencyIndex) ?: continue
            val currency = currencyBlock.stringValue("currency") ?: "CNY"
            val series = currencyBlock.optJSONArray("series") ?: JSONArray()
            for (seriesIndex in 0 until series.length()) {
                val item = series.optJSONObject(seriesIndex) ?: continue
                val apiKey = item.optJSONObject("api_key")
                val apiKeyId = apiKey?.stringValue("tracking_id") ?: "untracked"
                val apiKeyName = apiKey?.stringValue("name")
                val model = item.stringValue("model") ?: "unknown"
                val buckets = item.optJSONArray("buckets") ?: JSONArray()
                for (bucketIndex in 0 until buckets.length()) {
                    val bucket = buckets.optJSONObject(bucketIndex) ?: continue
                    records += CostRecord(
                        currency = currency,
                        apiKeyId = apiKeyId,
                        apiKeyName = apiKeyName,
                        model = model,
                        timeEpochSecond = bucket.longValue("time") ?: continue,
                        cost = bucket.doubleValue("cost") ?: 0.0,
                    )
                }
            }
        }
        return records
    }

    companion object {
        private fun defaultClient(): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(45, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }
}

private fun JSONObject.bizData(): JSONObject {
    val data = optJSONObject("data") ?: JSONObject()
    return data.optJSONObject("biz_data") ?: JSONObject()
}

private fun JSONObject.stringValue(name: String): String? {
    if (!has(name) || isNull(name)) return null
    return optString(name).takeIf { it.isNotBlank() && it != "null" }
}

private fun JSONObject.doubleValue(name: String): Double? {
    if (!has(name) || isNull(name)) return null
    return when (val value = opt(name)) {
        is Number -> value.toDouble()
        is String -> value.toDoubleOrNull()
        else -> null
    }
}

private fun JSONObject.longValue(name: String): Long? {
    if (!has(name) || isNull(name)) return null
    return when (val value = opt(name)) {
        is Number -> value.toLong()
        is String -> value.toLongOrNull()
        else -> null
    }
}

private fun JSONObject.intValue(name: String, fallback: Int): Int {
    if (!has(name) || isNull(name)) return fallback
    return when (val value = opt(name)) {
        is Number -> value.toInt()
        is String -> value.toIntOrNull() ?: fallback
        else -> fallback
    }
}

private fun JSONArray?.mapWallets(): List<WalletBalance> =
    this?.let { array ->
        buildList {
            for (index in 0 until array.length()) {
                val item = array.optJSONObject(index) ?: continue
                add(
                    WalletBalance(
                        currency = item.stringValue("currency") ?: "CNY",
                        balance = item.doubleValue("balance") ?: 0.0,
                        tokenEstimation = item.longValue("token_estimation"),
                    ),
                )
            }
        }
    } ?: emptyList()

private inline fun <T> JSONArray?.map(block: (JSONObject) -> T): List<T> =
    this?.let { array ->
        buildList {
            for (index in 0 until array.length()) {
                array.optJSONObject(index)?.let { add(block(it)) }
            }
        }
    } ?: emptyList()
