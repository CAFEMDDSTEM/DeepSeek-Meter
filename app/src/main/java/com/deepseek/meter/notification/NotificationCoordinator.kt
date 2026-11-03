package com.deepseek.meter.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.deepseek.meter.R
import com.deepseek.meter.data.model.AppSettings
import com.deepseek.meter.data.model.DashboardSnapshot
import com.deepseek.meter.data.store.SettingsStore
import com.deepseek.meter.ui.localization.S
import com.deepseek.meter.ui.localization.stringsFor
import com.deepseek.meter.util.formatAmount
import com.deepseek.meter.util.formatMoney
import java.time.LocalDate

class NotificationCoordinator(
    private val context: Context,
    private val settingsStore: SettingsStore,
) {
    suspend fun evaluate(snapshot: DashboardSnapshot, settings: AppSettings) {
        val balance = snapshot.primaryBalance ?: return
        val currency = balance.currency
        val strings = stringsFor(settings.languageCode)

        if (settings.todayAlertEnabled && balance.todayCost >= settings.todayAlertThreshold) {
            val date = LocalDate.now().toString()
            val key = "$date|${currency}|${settings.todayAlertThreshold}"
            if (settings.lastTodayAlertKey != key) {
                send(
                    strings = strings,
                    id = TODAY_ALERT_ID,
                    title = strings[S.todayAlertTitle],
                    text = strings.format(
                        S.todayAlertText,
                        "0" to formatMoney(balance.todayCost, currency),
                        "1" to formatMoney(settings.todayAlertThreshold, currency),
                    ),
                )
                settingsStore.setLastTodayAlertKey(key)
            }
        }

        if (settings.lowBalanceAlertEnabled && balance.remaining < settings.lowBalanceThreshold) {
            if (!settings.lowBalanceActive) {
                send(
                    strings = strings,
                    id = LOW_BALANCE_ID,
                    title = strings[S.lowBalanceTitle],
                    text = strings.format(
                        S.lowBalanceText,
                        "0" to formatMoney(balance.remaining, currency),
                        "1" to formatMoney(settings.lowBalanceThreshold, currency),
                    ),
                )
                settingsStore.setLowBalanceActive(true)
            }
        } else if (settings.lowBalanceActive) {
            settingsStore.setLowBalanceActive(false)
        }
    }

    private fun send(
        strings: com.deepseek.meter.ui.localization.AppStrings,
        id: Int,
        title: String,
        text: String,
    ) {
        try {
            if (Build.VERSION.SDK_INT >= 33 &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            createChannel(strings)
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()
            NotificationManagerCompat.from(context).notify(id, notification)
        } catch (_: Exception) {
            // Missing notification permission or an OEM notification error must not crash the app.
        }
    }

    private fun createChannel(strings: com.deepseek.meter.ui.localization.AppStrings) {
        val manager = context.getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            CHANNEL_ID,
            strings[S.notificationChannel],
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = strings[S.notificationChannelDesc]
        }
        manager.createNotificationChannel(channel)
    }

    private companion object {
        const val CHANNEL_ID = "deepseek_meter_alerts"
        const val TODAY_ALERT_ID = 4101
        const val LOW_BALANCE_ID = 4102
    }
}
