package com.github.singularity.ui.feature.sync

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.designsystem.components.DrawerIcon
import com.github.singularity.ui.designsystem.components.ScreenScaffold
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.refresh
import singularity.composeapp.generated.resources.sync

@Composable
fun SyncScreen() {
    val viewModel = koinViewModel<SyncViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SyncScreen(
        uiState = uiState,
        execute = { viewModel.execute(this) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SyncScreen(
    uiState: SyncUiState,
    execute: SyncIntent.() -> Unit,
) {
    ScreenScaffold(
        topBar = {
            TopAppBar(
                title = { Text(Res.string.sync.getString()) },
                navigationIcon = {
                    DrawerIcon { SyncIntent.OpenDrawer.execute() }
                },
            )
        },
    ) { ip ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(ip),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                AnimatedContent(
                    targetState = uiState.connectionState,
                ) { state ->
                    Text(
                        text = state.message,
                        fontSize = 16.sp,
                    )
                }
                IconButton(
                    onClick = { SyncIntent.RefreshConnection.execute() },
                ) {
                    Icon(
                        painter = Res.drawable.refresh.getPainter(),
                        contentDescription = Res.string.refresh.getString(),
                    )
                }
            }
        }
    }
}
