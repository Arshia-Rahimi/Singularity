package com.github.singularity.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.singularity.common.App
import com.github.singularity.shared.getPlatform

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            getPlatform().name.let(::println)
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
