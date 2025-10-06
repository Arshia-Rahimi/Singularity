package com.github.singularity.ui.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.designsystem.components.DrawerIcon
import com.github.singularity.ui.designsystem.components.ScreenScaffold
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.settings

@Composable
fun SettingsScreen(
    openDrawer: () -> Unit,
) {
    val viewModel = koinViewModel<SettingsViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        uiState = uiState,
        execute = {
            when (this) {
                is SettingsIntent.OpenDrawer -> openDrawer()
                else -> viewModel.execute(this)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    uiState: SettingsUiState,
    execute: SettingsIntent.() -> Unit,
) {
    ScreenScaffold(
        topBar = {
            TopAppBar(
                title = { Text(Res.string.settings.getString()) },
                navigationIcon = {
                    DrawerIcon { SettingsIntent.OpenDrawer.execute() }
                },
            )
        }
    ) { ip ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(ip)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { SettingsIntent.ToggleAppTheme.execute() }
                        .padding(vertical = 4.dp, horizontal = 8.dp),
                ) {
                    Text("theme: ${uiState.appTheme.title.getString()}")
                }
            }
        }
    }
}
