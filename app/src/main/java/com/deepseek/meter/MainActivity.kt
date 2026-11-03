package com.deepseek.meter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.deepseek.meter.ui.DeepSeekMeterApp
import com.deepseek.meter.ui.theme.DeepSeekMeterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeepSeekMeterTheme {
                DeepSeekMeterApp()
            }
        }
    }
}
