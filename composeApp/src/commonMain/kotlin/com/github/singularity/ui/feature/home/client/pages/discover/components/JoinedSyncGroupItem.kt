package com.github.singularity.ui.feature.home.client.pages.discover.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
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

@Composable
fun LazyGridItemScope.JoinedSyncGroupItem(
	joinedSyncGroup: JoinedSyncGroup,
	execute: ClientIntent.() -> Unit,
) {
	var showDeletionDialog by remember { mutableStateOf(false) }

	Box(
		modifier = Modifier
			.animateItem()
			.padding(8.dp)
			.clip(RoundedCornerShape(16.dp))
			.background(MaterialTheme.colorScheme.secondary)
			.combinedClickable(
				onClick = { ClientIntent.SetAsDefault(joinedSyncGroup).execute() },
				onLongClick = { showDeletionDialog = true },
			)
			.padding(16.dp),
		contentAlignment = Alignment.Center,
	) {
		Text(
			fontSize = 16.sp,
			text = joinedSyncGroup.syncGroupName,
			color = MaterialTheme.colorScheme.onSecondary,
		)
	}

	ConfirmationDialog(
		visible = showDeletionDialog,
		title = Res.string.confirm_action.getString(),
		message = Res.string.delete_group.getString(joinedSyncGroup.syncGroupName),
		onConfirm = { ClientIntent.DeleteGroup(joinedSyncGroup).execute() },
		onDismiss = { showDeletionDialog = false },
		confirmText = Res.string.delete.getString(),
	)
}
