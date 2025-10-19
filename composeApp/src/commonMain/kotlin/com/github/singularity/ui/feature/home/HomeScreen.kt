package com.github.singularity.ui.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.ui.feature.home.client.DiscoverScreen
import com.github.singularity.ui.feature.home.server.ServerScreen

@Composable
fun HomeScreen(
    syncMode: SyncMode,
) {
    AnimatedContent(syncMode) {
        when (it) {
            SyncMode.Server -> ServerScreen()
            SyncMode.Client -> DiscoverScreen()
        }
    }
}
