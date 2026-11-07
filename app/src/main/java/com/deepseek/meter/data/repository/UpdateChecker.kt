package com.deepseek.meter.data.repository

import com.deepseek.meter.data.model.AppUpdateInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class UpdateChecker(
    private val client: OkHttpClient = defaultClient(),
    private val releasesUrl: String = "https://api.github.com/repos/CAFEMDDSTEM/DeepSeek-Meter/releases/latest",
) {
    suspend fun latestUpdate(currentVersion: String): AppUpdateInfo? = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(releasesUrl.toHttpUrl())
            .header("Accept", "application/vnd.github+json")
            .header("User-Agent", "DeepSeekMeter-Android")
            .get()
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) error("GitHub API HTTP ${response.code}")
            val json = JSONObject(response.body?.string().orEmpty())
            if (json.optBoolean("draft", false) || json.optBoolean("prerelease", false)) return@use null
            val tag = json.optString("tag_name")
            val latest = tag.removePrefix("v").removePrefix("V")
            if (latest.isBlank() || compareVersions(latest, currentVersion) <= 0) return@use null
            val assets = json.optJSONArray("assets")
            var apkUrl: String? = null
            if (assets != null) {
                for (index in 0 until assets.length()) {
                    val asset = assets.optJSONObject(index) ?: continue
                    if (asset.optString("name").endsWith(".apk", ignoreCase = true)) {
                        apkUrl = asset.optString("browser_download_url")
                        break
                    }
                }
            }
            AppUpdateInfo(
                latestVersion = latest,
                releaseUrl = json.optString("html_url"),
                apkUrl = apkUrl,
                releaseNotes = json.optString("body"),
            )
        }
    }

    private fun compareVersions(left: String, right: String): Int {
        fun parts(value: String): List<Int> =
            value.split('.', '-', '+').mapNotNull { it.toIntOrNull() }
        val a = parts(left)
        val b = parts(right)
        val size = maxOf(a.size, b.size)
        for (index in 0 until size) {
            val av = a.getOrElse(index) { 0 }
            val bv = b.getOrElse(index) { 0 }
            if (av != bv) return av.compareTo(bv)
        }
        return 0
    }

    companion object {
        private fun defaultClient(): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(45, TimeUnit.SECONDS)
            .build()
    }
}
