package com.github.singularity.ui.feature.home.client.pages

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.singularity.core.shared.AppTheme
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.ui.designsystem.components.DrawerIcon
import com.github.singularity.ui.designsystem.theme.SingularityTheme
import com.github.singularity.ui.feature.home.client.ClientIntent
import com.github.singularity.ui.feature.home.client.ClientUiState
import com.github.singularity.ui.feature.home.client.components.JoinedSyncGroupItem
import org.jetbrains.compose.ui.tooling.preview.Preview
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.joined_sync_groups

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverPage(
	uiState: ClientUiState,
	execute: ClientIntent.() -> Unit,
	topBar: (@Composable () -> Unit) -> Unit,
) {
	topBar {
		TopAppBar(
			title = { Text(uiState.defaultSyncGroup?.syncGroupName ?: "") },
			navigationIcon = {
				DrawerIcon { ClientIntent.OpenDrawer.execute() }
			},
		)
	}

	LazyVerticalGrid(
		columns = GridCells.Adaptive(150.dp),
		modifier = Modifier.fillMaxSize()
			.padding(horizontal = 4.dp),
	) {
		stickyHeader(
			key = "joined_title",
		) {
			Row(
				modifier = Modifier.fillMaxWidth()
					.padding(vertical = 8.dp, horizontal = 12.dp),
			) {
				Text(Res.string.joined_sync_groups.getString())
			}
		}
		items(
			items = uiState.joinedSyncGroups,
			key = { it.syncGroupId },
			contentType = { it },
		) {
			JoinedSyncGroupItem(
				joinedSyncGroup = it,
				execute = execute,
			)
		}
	}
}

@Preview
@Composable
private fun Preview() {
	SingularityTheme(AppTheme.Light) {
		DiscoverPage(
			execute = {},
			uiState = ClientUiState(
				joinedSyncGroups = listOf(
					JoinedSyncGroup(
						authToken = "a",
						syncGroupId = "1",
						isDefault = false,
						syncGroupName = "name",
					),
					JoinedSyncGroup(
						authToken = "a",
						syncGroupId = "2",
						isDefault = false,
						syncGroupName = "name",
					),
					JoinedSyncGroup(
						authToken = "a",
						syncGroupId = "3",
						isDefault = false,
						syncGroupName = "name",
					),
					JoinedSyncGroup(
						authToken = "a",
						syncGroupId = "4",
						isDefault = false,
						syncGroupName = "name",
					),
				).toMutableStateList(),
			),
			topBar = {},
		)
	}
}
