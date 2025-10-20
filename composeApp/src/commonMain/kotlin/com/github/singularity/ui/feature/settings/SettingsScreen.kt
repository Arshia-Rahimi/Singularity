package com.github.singularity.ui.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.core.shared.compose.onCondition
import com.github.singularity.core.shared.compose.select
import com.github.singularity.core.shared.getScaleLabel
import com.github.singularity.core.shared.scaleOptions
import com.github.singularity.ui.designsystem.components.DrawerIcon
import com.github.singularity.ui.designsystem.components.ScreenScaffold
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.arrow_down
import singularity.composeapp.generated.resources.arrow_up
import singularity.composeapp.generated.resources.settings

@Composable
fun SettingsScreen() {
    val viewModel = koinViewModel<SettingsViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        uiState = uiState,
        execute = { viewModel.execute(this) },
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
                .padding(ip),
        ) {
            settingsItem(
                onClick = { SettingsIntent.ToggleTheme.execute() },
            ) {
                SettingsItemText("Theme: ")
                SettingsItemText(uiState.theme.title.getString())
            }

            settingsItem {
                var expanded by remember { mutableStateOf(false) }
                SettingsItemText("Scale:")

                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    SettingsItemText(uiState.scale.getScaleLabel())
                    IconButton(
                        onClick = { expanded = true },
                    ) {
                        Icon(
                            painter = expanded.select(
                                Res.drawable.arrow_down,
                                Res.drawable.arrow_up,
                            ).getPainter(),
                            contentDescription = "",
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            scaleOptions.forEach {
                                DropdownMenuItem(
                                    text = { Text(it.getScaleLabel()) },
                                    onClick = {
                                        SettingsIntent.ChangeScale(it).execute()
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}


private fun LazyListScope.settingsItem(
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .onCondition(onClick != null) {
                    clickable { onClick?.invoke() }
                }
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            content()
        }
    }
}

@Composable
private fun SettingsItemText(
    text: String,
) {
    Text(
        text = text,
        fontSize = 16.sp,
    )
}
