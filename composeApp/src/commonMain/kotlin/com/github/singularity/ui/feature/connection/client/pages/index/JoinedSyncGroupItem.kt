package com.github.singularity.ui.feature.connection.client.pages.index

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.ui.designsystem.shared.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.designsystem.shared.getPainter
import com.github.singularity.ui.designsystem.shared.getString
import com.github.singularity.ui.feature.connection.client.ClientIntent
import com.github.singularity.ui.feature.connection.client.PairedSyncGroup
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.active
import singularity.composeapp.generated.resources.confirm_action
import singularity.composeapp.generated.resources.delete
import singularity.composeapp.generated.resources.delete_group
import singularity.composeapp.generated.resources.options

@Composable
fun LazyGridItemScope.JoinedSyncGroupItem(
	joinedSyncGroup: PairedSyncGroup,
	execute: ClientIntent.() -> Unit,
) {
	var showDeletionDialog by remember { mutableStateOf(false) }
	var showDropDownMenu by remember { mutableStateOf(false) }

	Row(
		modifier = Modifier
			.animateItem()
			.padding(8.dp)
			.clip(RoundedCornerShape(16.dp))
			.background(MaterialTheme.colorScheme.secondaryContainer)
			.clickable { ClientIntent.SetAsDefault(joinedSyncGroup).execute() }
			.padding(16.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
	) {
		Row {
			Text(
				fontSize = 16.sp,
				text = joinedSyncGroup.groupName,
				color = MaterialTheme.colorScheme.onSecondaryContainer,
			)
			if (joinedSyncGroup.isAvailable) {
				Icon(
					modifier = Modifier.padding(start = 4.dp),
					painter = Res.drawable.active.getPainter(),
					contentDescription = Res.string.active.getString(),
					tint = MaterialTheme.colorScheme.inversePrimary,
				)
			}
		}

		Box {
			IconButton(
				onClick = { showDropDownMenu = true },
			) {
				Icon(
					painter = Res.drawable.options.getPainter(),
					contentDescription = Res.string.options.getString(),
				)
			}
			DropdownMenu(
				expanded = showDropDownMenu,
				onDismissRequest = { showDropDownMenu = false },
			) {
				DropdownMenuItem(
					text = { Text(Res.string.delete.getString()) },
					onClick = {
						showDeletionDialog = true
						showDropDownMenu = false
					},
					trailingIcon = {
						Icon(
							painter = Res.drawable.delete.getPainter(),
							contentDescription = Res.string.delete.getString(),
						)
					},
				)
			}
		}
	}

	ConfirmationDialog(
		visible = showDeletionDialog,
		title = Res.string.confirm_action.getString(),
		message = Res.string.delete_group.getString(joinedSyncGroup.groupName),
		onConfirm = { ClientIntent.DeleteGroup(joinedSyncGroup).execute() },
		onDismiss = { showDeletionDialog = false },
		confirmText = Res.string.delete.getString(),
	)
}
