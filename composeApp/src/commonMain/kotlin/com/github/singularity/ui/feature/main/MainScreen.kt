package com.github.singularity.ui.feature.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.singularity.core.mdns.canHostSyncServer
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    toDiscoverScreen: () -> Unit,
    toBroadcastScreen: () -> Unit,
    toSettingsScreen: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(
                        onClick = toSettingsScreen,
                    ) {
                        Icon(
                            painter = Res.drawable.settings.getPainter(),
                            contentDescription = Res.string.settings.getString(),
                        )
                    }
                }
            )
        },
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
