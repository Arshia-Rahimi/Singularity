package com.github.singularity.ui.feature.plugins

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
		LazyVerticalGrid(
			modifier = Modifier.fillMaxSize()
				.padding(ip)
				.padding(horizontal = 4.dp),
			columns = GridCells.Adaptive(350.dp)
		) {
			items(uiState.plugins) { plugin ->
				Row(
					modifier = Modifier
						.animateItem()
						.fillMaxWidth()
						.clip(RoundedCornerShape(20))
						.background(if (plugin.isEnabled) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer)
						.padding(horizontal = 10.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically,
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
