package com.github.singularity.ui.feature.home.client.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.ui.designsystem.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.feature.home.client.ClientIntent
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.confirm_action
import singularity.composeapp.generated.resources.delete
import singularity.composeapp.generated.resources.delete_group
import singularity.composeapp.generated.resources.set_group_as_default

@Composable
fun JoinedSyncGroupItem(
	joinedSyncGroup: JoinedSyncGroup,
	execute: ClientIntent.() -> Unit,
) {
	var showSetAsDefaultDialog by remember { mutableStateOf(false) }
	var showDeletionDialog by remember { mutableStateOf(false) }

	Box(
		modifier = Modifier
			.padding(4.dp)
			.clip(RoundedCornerShape(16.dp))
			.background(MaterialTheme.colorScheme.secondary)
			.padding(vertical = 4.dp, horizontal = 8.dp),
		contentAlignment = Alignment.Center,
	) {
		Text(
			fontSize = 16.sp,
			text = joinedSyncGroup.syncGroupName,
		)
	}

	ConfirmationDialog(
		visible = showSetAsDefaultDialog,
		message = Res.string.set_group_as_default.getString(joinedSyncGroup.syncGroupName),
		onConfirm = { ClientIntent.SetAsDefault(joinedSyncGroup).execute() },
		onDismiss = { showSetAsDefaultDialog = false },
	)

	ConfirmationDialog(
		visible = showDeletionDialog,
		title = Res.string.confirm_action.getString(),
		message = Res.string.delete_group.getString(joinedSyncGroup.syncGroupName),
		onConfirm = { ClientIntent.DeleteGroup(joinedSyncGroup).execute() },
		onDismiss = { showDeletionDialog = false },
		confirmText = Res.string.delete.getString(),
	)
}
