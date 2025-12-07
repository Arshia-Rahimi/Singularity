package com.github.singularity.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.singularity.core.syncservice.AndroidApplicationService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val serviceIntent = Intent(this, AndroidApplicationService::class.java)
        startForegroundService(serviceIntent)

        setContent {
            App()
        }
    }
}
