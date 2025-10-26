package com.github.singularity.ui.feature.connection

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.ui.feature.connection.client.ClientScreen
import com.github.singularity.ui.feature.connection.server.ServerScreen

@Composable
fun HomeScreen(
    syncMode: SyncMode,
) {
    AnimatedContent(syncMode) {
        when (it) {
            SyncMode.Server -> ServerScreen()
            SyncMode.Client -> ClientScreen()
        }
    }
}
