package com.github.singularity.ui.navigation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { ip ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(ip),
            contentAlignment = Alignment.Center,
        ) {
            LinearProgressIndicator()
        }
    }
}
