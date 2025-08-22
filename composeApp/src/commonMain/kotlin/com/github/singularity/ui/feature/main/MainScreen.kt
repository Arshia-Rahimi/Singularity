package com.github.singularity.ui.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MainScreen(
    toDiscoverScreen: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { ip ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(ip),
            contentAlignment = Alignment.Center,
        ) {
            Button(
                onClick = toDiscoverScreen,
            ) {
                Text("discover")
            }
        }
    }
}
