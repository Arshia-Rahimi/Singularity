package com.github.singularity.ui.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.ui.feature.home.broadcast.BroadcastScreen
import com.github.singularity.ui.feature.home.discover.DiscoverScreen

@Composable
fun HomeScreen(
    syncMode: SyncMode,
) {
    AnimatedContent(syncMode) {
        when (it) {
            SyncMode.Server -> BroadcastScreen()
            SyncMode.Client -> DiscoverScreen()
        }
    }
}
