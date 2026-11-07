package com.deepseek.meter.data.repository

import com.deepseek.meter.data.model.DeepSeekServiceStatus
import com.deepseek.meter.data.model.ServiceHealth
import com.deepseek.meter.data.model.StatusComponent
import com.deepseek.meter.data.model.StatusIncident
import com.deepseek.meter.data.model.StatusSection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant
import java.util.concurrent.TimeUnit

class StatusRepository(
    private val client: OkHttpClient = defaultClient(),
    private val baseUrl: String = "https://cn.statuspage.flashduty.com/api/status-page/6410630422455",
) {
    suspend fun fetchStatus(): DeepSeekServiceStatus = coroutineScope {
        val now = Instant.now().epochSecond
        val active = async { fetchActiveSummary() }
        val history = async { runCatching { fetchHistory(now) }.getOrDefault(emptyList()) }
        val uptimes = async { runCatching { fetchUptimes(now) }.getOrDefault(emptyMap()) }
        buildStatus(active.await(), history.await(), uptimes.await())
    }

    private suspend fun fetchActiveSummary(): JSONObject = getJson("$baseUrl/summary/active")

    private suspend fun fetchHistory(now: Long): List<StatusIncident> {
        val start = now - 30L * 24 * 60 * 60
        val json = getJson("$baseUrl/change/list?start_at_seconds=$start&end_at_seconds=$now")
        val items = json.optJSONObject("data")?.optJSONArray("items") ?: JSONArray()
        return buildList {
            for (index in 0 until items.length()) {
                val item = items.optJSONObject(index) ?: continue
                add(parseIncident(item, "change_id"))
            }
        }
    }

    private suspend fun fetchUptimes(now: Long): Map<String, Double> {
        val start = now - 90L * 24 * 60 * 60
        val json = getJson("$baseUrl/summary/structure?start_at_from_seconds=$start&start_at_to_seconds=$now")
        val data = json.optJSONObject("data") ?: return emptyMap()
        val result = mutableMapOf<String, Double>()
        val array = data.optJSONArray("component_uptimes") ?: JSONArray()
        for (index in 0 until array.length()) {
            val item = array.optJSONObject(index) ?: continue
            val id = item.optString("component_id")
            if (id.isNotBlank()) result[id] = item.optDouble("uptime", 0.0)
        }
        return result
    }

    private suspend fun getJson(url: String): JSONObject = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(url.toHttpUrl())
            .header("Accept", "application/json")
            .header("User-Agent", "DeepSeekMeter/1.2 Android")
            .get()
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) error("Status API HTTP ${response.code}")
            JSONObject(response.body?.string().orEmpty())
        }
    }

    private fun buildStatus(
        activeJson: JSONObject,
        history: List<StatusIncident>,
        uptimes: Map<String, Double>,
    ): DeepSeekServiceStatus {
        val data = activeJson.optJSONObject("data")
        val page = data?.optJSONObject("page")
        val activeChanges = data?.optJSONArray("active_changes") ?: JSONArray()
        val componentHealth = mutableMapOf<String, ServiceHealth>()
        val activeIncidents = mutableListOf<StatusIncident>()

        for (index in 0 until activeChanges.length()) {
            val change = activeChanges.optJSONObject(index) ?: continue
            val incident = parseIncident(change, "change_id")
            activeIncidents += incident
            val affected = change.optJSONArray("affected_components") ?: JSONArray()
            for (affectedIndex in 0 until affected.length()) {
                val component = affected.optJSONObject(affectedIndex) ?: continue
                val id = component.optString("component_id")
                if (id.isNotBlank()) {
                    componentHealth[id] = maxHealth(
                        componentHealth[id] ?: ServiceHealth.Operational,
                        healthFrom(component.optString("status")),
                    )
                }
            }
            val changeHealth = healthFrom(change.optString("status"))
            if (affected.length() == 0 && changeHealth == ServiceHealth.Unknown) {
                activeIncidents += incident
            }
        }

        val allComponents = mutableListOf<StatusComponent>()
        val components = page?.optJSONArray("components") ?: JSONArray()
        for (index in 0 until components.length()) {
            val item = components.optJSONObject(index) ?: continue
            val id = item.optString("component_id")
            allComponents += StatusComponent(
                id = id,
                name = displayName(item.optString("name")),
                description = displayName(item.optString("description")),
                sectionId = item.optString("section_id").takeIf { it.isNotBlank() },
                health = componentHealth[id] ?: ServiceHealth.Operational,
                uptimePercent = uptimes[id],
            )
        }

        val sections = mutableListOf<StatusSection>()
        val sectionArray = page?.optJSONArray("sections") ?: JSONArray()
        for (index in 0 until sectionArray.length()) {
            val item = sectionArray.optJSONObject(index) ?: continue
            val id = item.optString("section_id")
            val sectionComponents = allComponents.filter { it.sectionId == id }
            if (sectionComponents.isNotEmpty()) {
                sections += StatusSection(id, displayName(item.optString("name")), sectionComponents)
            }
        }

        val overall = if (activeIncidents.isEmpty()) {
            ServiceHealth.Operational
        } else {
            componentHealth.values.fold(ServiceHealth.Operational, ::maxHealth).let {
                if (it == ServiceHealth.Operational) ServiceHealth.Degraded else it
            }
        }

        return DeepSeekServiceStatus(
            overall = overall,
            sections = sections,
            ungroupedComponents = allComponents.filter { it.sectionId == null },
            activeIncidents = activeIncidents,
            recentIncidents = history.take(10),
            lastUpdated = Instant.now(),
        )
    }

    private fun parseIncident(item: JSONObject, idKey: String): StatusIncident {
        val affected = item.optJSONArray("affected_components") ?: JSONArray()
        val names = buildList {
            for (index in 0 until affected.length()) {
                val component = affected.optJSONObject(index) ?: continue
                val name = displayName(component.optString("name"))
                if (name.isNotBlank()) add(name)
            }
        }
        return StatusIncident(
            id = item.opt(idKey)?.toString().orEmpty(),
            title = displayName(item.optString("title")),
            status = item.optString("status"),
            description = displayName(item.optString("description")),
            startEpochSecond = item.optLong("start_at_seconds").takeIf { it > 0 },
            closeEpochSecond = item.optLong("close_at_seconds").takeIf { it > 0 },
            affectedComponents = names,
        )
    }

    private fun displayName(value: String): String {
        if (value.isBlank()) return value
        val chinese = value.substringBefore("(").trim()
        val english = value.substringAfter("(", "").substringBefore(")").trim()
        return if (chinese.isNotBlank() && english.isNotBlank()) "$chinese / $english" else value
    }

    private fun healthFrom(value: String): ServiceHealth = when (value.lowercase()) {
        "operational" -> ServiceHealth.Operational
        "degraded" -> ServiceHealth.Degraded
        "partial_outage" -> ServiceHealth.PartialOutage
        "full_outage" -> ServiceHealth.FullOutage
        "maintenance" -> ServiceHealth.Maintenance
        else -> ServiceHealth.Unknown
    }

    private fun maxHealth(a: ServiceHealth, b: ServiceHealth): ServiceHealth {
        fun rank(value: ServiceHealth) = when (value) {
            ServiceHealth.Operational -> 0
            ServiceHealth.Degraded -> 1
            ServiceHealth.Maintenance, ServiceHealth.PartialOutage -> 2
            ServiceHealth.FullOutage -> 3
            ServiceHealth.Unknown -> 1
        }
        return if (rank(b) > rank(a)) b else a
    }

    companion object {
        private fun defaultClient(): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(45, TimeUnit.SECONDS)
            .build()
    }
}
