package com.deepseek.meter

import android.app.Application
import com.deepseek.meter.work.BackgroundRefreshScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DeepSeekMeterApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            BackgroundRefreshScheduler.schedule(this@DeepSeekMeterApplication)
        }
    }
}
