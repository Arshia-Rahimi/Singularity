package com.github.singularity.ui.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.core.shared.compose.select
import com.github.singularity.ui.designsystem.components.DrawerIcon
import com.github.singularity.ui.designsystem.components.ScreenScaffold
import com.github.singularity.core.shared.ScaleOption
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
				.padding(ip)
		) {
			settingsItem(
				onClick = { SettingsIntent.ToggleTheme.execute() },
			) {
				Text("theme: ${uiState.theme.title.getString()}")
			}

			settingsItem {
				var expanded by remember { mutableStateOf(false) }
				Text("scale: ")
				Box(modifier = Modifier.weight(0.5f)) {
					OutlinedTextField(
						enabled = false,
						value = uiState.scale.label,
						modifier = Modifier.fillMaxWidth(),
						onValueChange = {},
						trailingIcon = {
							Icon(
								painter = expanded.select(
									Res.drawable.arrow_down,
									Res.drawable.arrow_up,
								).getPainter(),
								contentDescription = "",
							)
						},
						readOnly = true,
					)

					Surface(
						modifier = Modifier
							.fillMaxSize()
							.padding(top = 8.dp)
							.clip(MaterialTheme.shapes.extraSmall)
							.clickable { expanded = true },
						color = Color.Transparent,
					) {}
				}

				if (expanded) {
					Dialog(
						onDismissRequest = { expanded = false },
					) {
						Surface(
							shape = RoundedCornerShape(12.dp),
						) {
							LazyColumn(modifier = Modifier.fillMaxWidth()) {
								items(ScaleOption.entries) { item ->
									val contentColor =
										if (item == uiState.scale) MaterialTheme.colorScheme.primary
										else MaterialTheme.colorScheme.onSurface

									CompositionLocalProvider(LocalContentColor provides contentColor) {
										Box(
											modifier = Modifier
												.clickable {
													SettingsIntent.ChangeScale(item).execute()
													expanded = false
												}
												.fillMaxWidth()
												.padding(16.dp)) {
											Text(
												text = item.label,
												style = MaterialTheme.typography.titleSmall,
											)
										}
										HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
									}
								}
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
	horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
	content: @Composable RowScope.() -> Unit,
) {
	item {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.clickable(onClick != null, onClick = onClick ?: {})
				.padding(vertical = 4.dp, horizontal = 8.dp),
			content = content,
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = horizontalArrangement,
		)
	}
}
