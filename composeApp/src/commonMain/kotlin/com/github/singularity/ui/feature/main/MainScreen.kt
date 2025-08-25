package com.github.singularity.ui.feature.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.singularity.core.mdns.canHostSyncServer

@Composable
fun MainScreen(
    toDiscoverScreen: () -> Unit,
    toBroadcastScreen: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { ip ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(ip),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = toDiscoverScreen,
            ) {
                Text("discover")
            }
            if (canHostSyncServer) {
                Button(
                    onClick = toBroadcastScreen,
                ) {
                    Text("broadcast")
                }
            }
        }
    }
}
