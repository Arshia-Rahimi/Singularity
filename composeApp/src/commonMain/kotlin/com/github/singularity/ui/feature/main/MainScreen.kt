package com.github.singularity.ui.feature.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.shared.canHostSyncServer
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.settings
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
fun MainScreen(
    toDiscoverScreen: () -> Unit,
    toBroadcastScreen: () -> Unit,
    toSettingsScreen: () -> Unit,
) {
    val viewModel = koinViewModel<MainViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val testEvent by viewModel.events.collectAsStateWithLifecycle()

    MainScreen(
        uiState = uiState,
        execute = {
            when (this) {
                is MainIntent.ToDiscoverScreen -> toDiscoverScreen()
                is MainIntent.ToSettingsScreen -> toSettingsScreen()
                is MainIntent.ToBroadcastScreen -> if (canHostSyncServer) toBroadcastScreen()
                else -> viewModel.execute(this)
            }
        },
        sendEvent = viewModel::sendEvent,
        events = testEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
private fun MainScreen(
    uiState: MainUiState,
    execute: MainIntent.() -> Unit,
    sendEvent: () -> Unit,
    events: List<TestEvent>,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(
                        onClick = { MainIntent.ToSettingsScreen.execute() },
                    ) {
                        Icon(
                            painter = Res.drawable.settings.getPainter(),
                            contentDescription = Res.string.settings.getString(),
                        )
                    }
                },
            )
        },
    ) { ip ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(ip),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedContent(uiState.connectionState.message) {
                Text(it)

                Button(
                    modifier = Modifier.padding(4.dp),
                    onClick = { MainIntent.RefreshConnection.execute() },
                ) {
                    Text("refresh")
                }
            }

            // test
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(events) {
                    val time = Instant.fromEpochMilliseconds(it.time)
                        .toLocalDateTime(TimeZone.currentSystemDefault()).time
                    Text("${time.hour}:${time.minute}:${time.second}")
                }
                item {
                    Button(onClick = sendEvent) {
                        Text("send")
                    }
                }
            }
            // test end

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = { MainIntent.ToDiscoverScreen.execute() },
                ) {
                    Text("discover")
                }
                if (canHostSyncServer) {
                    Button(
                        onClick = { MainIntent.ToBroadcastScreen.execute() },
                    ) {
                        Text("broadcast")
                    }
                }
            }

            if (canHostSyncServer) {
                Switch(
                    checked = uiState.syncMode == SyncMode.Server,
                    onCheckedChange = { MainIntent.ToggleSyncMode.execute() },
                )
            } else Spacer(Modifier)
        }
    }
}
