package com.github.singularity.ui.feature.connection.server.pages.index

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.ui.designsystem.components.Grid
import com.github.singularity.ui.designsystem.components.TopBar
import com.github.singularity.ui.designsystem.shared.components.dialogs.InputDialog
import com.github.singularity.ui.designsystem.shared.getPainter
import com.github.singularity.ui.designsystem.shared.getString
import com.github.singularity.ui.feature.connection.server.ServerIntent
import com.github.singularity.ui.feature.connection.server.ServerUiState
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.available_sync_groups
import singularity.composeapp.generated.resources.create
import singularity.composeapp.generated.resources.create_new_sync_group
import singularity.composeapp.generated.resources.no_sync_groups
import singularity.composeapp.generated.resources.plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostedSyncGroupIndexPage(
	uiState: ServerUiState,
	execute: ServerIntent.() -> Unit,
) {
	var showCreateGroupDialog by remember { mutableStateOf(false) }
	val focusManager = LocalFocusManager.current

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			TopBar(
				title = Res.string.available_sync_groups.getString(),
			)
		},
	) { ip ->

		Grid(ip) {

			hostedSyncGroupItems(uiState, execute)

			createGroupItem(execute) { showCreateGroupDialog = true }

		}

		InputDialog(
			visible = showCreateGroupDialog,
			onConfirm = { ServerIntent.CreateGroup(it).execute() },
			onDismiss = {
				showCreateGroupDialog = false
				focusManager.clearFocus()
			},
			confirmText = Res.string.create.getString(),
			title = Res.string.create_new_sync_group.getString(),
			keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
		)
	}
}

private fun LazyGridScope.hostedSyncGroupItems(
	uiState: ServerUiState,
	execute: ServerIntent.() -> Unit,
) {
	if (uiState.hostedSyncGroups.isEmpty()) {
		stickyHeader(
			key = "no_sync_group_title",
		) {
			Row(
				modifier = Modifier
					.animateItem()
					.fillMaxWidth()
					.padding(vertical = 8.dp, horizontal = 12.dp),
				horizontalArrangement = Arrangement.Center,
			) {
				Text(
					text = Res.string.no_sync_groups.getString(),
					fontSize = 12.sp,
				)
			}
		}
	} else {

		items(
			items = uiState.hostedSyncGroups,
			key = { "hosted_${it.hostedSyncGroupId}" },
			contentType = { it::class },
		) {
			HostedSyncGroupItem(
				hostedSyncGroup = it,
				execute = execute,
			)
		}
	}
}

private fun LazyGridScope.createGroupItem(
	execute: ServerIntent.() -> Unit,
	showCreateGroupDialog: () -> Unit,
) {
	stickyHeader(
		key = "createGroup_title",
		contentType = "title"
	) {
		Row(
			modifier = Modifier
				.animateItem()
				.fillMaxWidth()
				.padding(vertical = 8.dp)
				.padding(bottom = 8.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Center,
		) {
			IconButton(
				onClick = showCreateGroupDialog,
			) {
				Icon(
					painter = Res.drawable.plus.getPainter(),
					contentDescription = "discover"
				)
			}
		}
	}
}
