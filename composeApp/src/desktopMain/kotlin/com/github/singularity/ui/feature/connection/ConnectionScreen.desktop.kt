package com.github.singularity.ui.feature.connection

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.syncservice.SyncService
import com.github.singularity.ui.feature.connection.client.ClientScreen
import com.github.singularity.ui.feature.connection.server.ServerScreen
import org.koin.compose.koinInject

@Composable
actual fun ConnectionScreen() {
	val syncService = koinInject<SyncService>()
	val syncMode by syncService.syncMode.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = syncMode,
        modifier = Modifier.fillMaxSize(),
    ) {
        when (it) {
            SyncMode.Server -> ServerScreen()
            SyncMode.Client -> ClientScreen()
        }
    }

}
