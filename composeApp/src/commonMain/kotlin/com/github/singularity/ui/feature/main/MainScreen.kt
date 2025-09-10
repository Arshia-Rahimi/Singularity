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
import com.github.singularity.core.shared.canHostSyncServer
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.settings

@Composable
fun MainScreen(
    toDiscoverScreen: () -> Unit,
    toBroadcastScreen: () -> Unit,
    toSettingsScreen: () -> Unit,
) {
    val viewModel = koinViewModel<MainViewModel>()
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainScreen(
//        uiState = uiState,
        execute = {
            when (this) {
                is MainIntent.ToDiscoverScreen -> toDiscoverScreen()
                is MainIntent.ToSettingsScreen -> toSettingsScreen()
                is MainIntent.ToBroadcastScreen -> toBroadcastScreen()
                else -> viewModel.execute(this)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
//    uiState: MainUiState,
    execute: MainIntent.() -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
//                    AnimatedContent(
//                        targetState = uiState.connectionState,
//                        transitionSpec = { slideInVertically() togetherWith slideOutVertically() }
//                    ) {
//                        Text(it.message.getString())
//                    }
                },
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
            verticalArrangement = Arrangement.Center,
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
    }
}
