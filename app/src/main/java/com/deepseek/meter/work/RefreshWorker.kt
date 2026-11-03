package com.deepseek.meter.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.deepseek.meter.data.api.ApiException
import com.deepseek.meter.data.repository.DashboardRepository
import com.deepseek.meter.data.store.SecureTokenStore
import com.deepseek.meter.data.store.SettingsStore
import com.deepseek.meter.notification.NotificationCoordinator
import java.util.concurrent.TimeUnit

class RefreshWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val settingsStore = SettingsStore(applicationContext)
        val settings = settingsStore.current()
        if (settings.refreshIntervalSeconds <= 0) return Result.success()

        val tokenStore = SecureTokenStore(applicationContext)
        val token = tokenStore.readToken() ?: return Result.success()

        return try {
            val snapshot = DashboardRepository().loadDashboard(token)
            NotificationCoordinator(applicationContext, settingsStore).evaluate(snapshot, settings)
            Result.success()
        } catch (error: ApiException) {
            if (error.code == 40002 || error.code == 40003) {
                tokenStore.clearToken()
                Result.success()
            } else {
                Result.retry()
            }
        } catch (_: Exception) {
            Result.retry()
        }
    }
}

object BackgroundRefreshScheduler {
    private const val UNIQUE_WORK_NAME = "deepseek_meter_periodic_refresh"

    fun schedule(context: Context) {
        val request = PeriodicWorkRequestBuilder<RefreshWorker>(15, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request,
        )
    }
}
