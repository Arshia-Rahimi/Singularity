package com.github.singularity.ui.feature.permissions

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.ui.designsystem.components.TopBar
import com.github.singularity.ui.designsystem.shared.getString
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.permissions

@Composable
fun PermissionsScreen() {
    val viewModel = koinViewModel<PermissionsViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PermissionsScreen(
        uiState = uiState,
        execute = { viewModel.execute(this) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PermissionsScreen(
    uiState: PermissionUiState,
    execute: PermissionsIntent.() -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = Res.string.permissions.getString(),
            )
        },
    ) { ip ->

    }
}
