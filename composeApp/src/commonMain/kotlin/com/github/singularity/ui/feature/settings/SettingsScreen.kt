package com.github.singularity.ui.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.getScaleLabel
import com.github.singularity.core.shared.scaleOptions
import com.github.singularity.ui.designsystem.components.TopBar
import com.github.singularity.ui.designsystem.shared.getPainter
import com.github.singularity.ui.designsystem.shared.getString
import com.github.singularity.ui.designsystem.shared.onCondition
import com.github.singularity.ui.feature.settings.components.ColorsList
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.arrow_down
import singularity.composeapp.generated.resources.arrow_up
import singularity.composeapp.generated.resources.primary_color
import singularity.composeapp.generated.resources.scale
import singularity.composeapp.generated.resources.settings
import singularity.composeapp.generated.resources.theme_options

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
	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = { TopBar(Res.string.settings.getString()) },
	) { ip ->
		LazyColumn(
			modifier = Modifier.fillMaxSize()
				.padding(ip),
		) {
            settingsItemRow {
                var expanded by remember { mutableStateOf(false) }
                SettingsItemText(Res.string.scale.getString())

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
                            painter = if (expanded) Res.drawable.arrow_down.getPainter() else Res.drawable.arrow_up.getPainter(),
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

            settingsItemRow(
                onClick = { SettingsIntent.ToggleThemeOption.execute() },
            ) {
                SettingsItemText(Res.string.theme_options.getString())
                SettingsItemText(uiState.themeOption.title.getString())
            }

            settingsItemColumn {
                SettingsItemText(Res.string.primary_color.getString())
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = ColorsList,
                        key = { it.value },
                        contentType = { Color::class }
                    ) {
                        Box(
                            modifier = Modifier.size(40.dp)
                                .clip(CircleShape)
                                .background(it)
                                .clickable { SettingsIntent.ChangePrimaryColor(it).execute() },
                            content = {},
                        )
                    }
                }
            }
		}
	}
}


private fun LazyListScope.settingsItemRow(
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

private fun LazyListScope.settingsItemColumn(
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .onCondition(onClick != null) {
                    clickable { onClick?.invoke() }
                }
                .padding(horizontal = 20.dp),
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
