package com.github.singularity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.singularity.app.App
import com.github.singularity.components.startAndroidApplicationService

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        startAndroidApplicationService()

        setContent {
            App()
        }
    }

}
