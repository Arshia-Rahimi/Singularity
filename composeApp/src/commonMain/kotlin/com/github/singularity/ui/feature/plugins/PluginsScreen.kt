package com.github.singularity.ui.feature.plugins

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.ui.designsystem.components.TopBar
import com.github.singularity.ui.designsystem.shared.getString
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.plugins

@Composable
fun PluginsScreen() {
	val viewModel = koinViewModel<PluginsViewModel>()
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	PluginsScreen(
		uiState = uiState,
		execute = { viewModel.execute(this) },
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PluginsScreen(
	uiState: PluginsUiState,
	execute: PluginsIntent.() -> Unit,
) {
	Scaffold(
		topBar = {
			TopBar(
				title = Res.string.plugins.getString(),
			)
		},
	) { ip ->
		LazyColumn(
			modifier = Modifier.fillMaxSize()
				.padding(ip),
			verticalArrangement = Arrangement.spacedBy(4.dp),
		) {
			items(uiState.plugins) { plugin ->
				Row(
					modifier = Modifier.fillMaxWidth()
						.height(30.dp)
						.clip(RoundedCornerShape(20))
						.background(if (plugin.isEnabled) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer),
					horizontalArrangement = Arrangement.SpaceBetween,
				) {
					Text(
						text = plugin.name,
						fontSize = 16.sp,
					)

					Switch(
						checked = plugin.isEnabled,
						onCheckedChange = { PluginsIntent.ToggleIsEnabled(plugin.name).execute() }
					)
				}
			}
		}
	}
}
