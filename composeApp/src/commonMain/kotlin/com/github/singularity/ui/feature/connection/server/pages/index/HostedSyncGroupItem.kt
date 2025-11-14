package com.github.singularity.ui.feature.connection.server.pages.index

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.ui.designsystem.shared.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.designsystem.shared.components.dialogs.InputDialog
import com.github.singularity.ui.designsystem.shared.getPainter
import com.github.singularity.ui.designsystem.shared.getString
import com.github.singularity.ui.designsystem.shared.onCondition
import com.github.singularity.ui.feature.connection.server.ServerIntent
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.confirm_action
import singularity.composeapp.generated.resources.delete
import singularity.composeapp.generated.resources.delete_group
import singularity.composeapp.generated.resources.edit
import singularity.composeapp.generated.resources.edit_group_name
import singularity.composeapp.generated.resources.nodes_paired
import singularity.composeapp.generated.resources.options

@Composable
fun LazyGridItemScope.HostedSyncGroupItem(
	hostedSyncGroup: HostedSyncGroup,
	execute: ServerIntent.() -> Unit,
) {
	var showDropDownMenu by remember { mutableStateOf(false) }
	var showDeletionDialog by remember { mutableStateOf(false) }
	var showEditDialog by remember { mutableStateOf(false) }

	val focusManager = LocalFocusManager.current

	val containerColor = MaterialTheme.colorScheme.secondaryContainer
	val textColor = MaterialTheme.colorScheme.onSecondaryContainer

	Box(
		modifier = Modifier
			.animateItem()
			.padding(8.dp)
			.clip(RoundedCornerShape(16.dp))
			.background(containerColor)
			.onCondition(!hostedSyncGroup.isDefault) {
				combinedClickable(
                    onClick = { ServerIntent.SetAsDefault(hostedSyncGroup).execute() },
					onLongClick = { showDeletionDialog = true },
				)
			}
			.padding(16.dp),
		contentAlignment = Alignment.Center,
	) {
		Row(
			modifier = Modifier.fillMaxSize(),
			horizontalArrangement = Arrangement.SpaceBetween,
		) {
			Column {
				Text(
					fontSize = 16.sp,
					text = hostedSyncGroup.name,
					color = textColor,
				)

				Text(
					text = "${hostedSyncGroup.nodes.size} ${Res.string.nodes_paired.getString()}",
					fontSize = 12.sp,
					color = textColor,
				)
			}

			Box {
				IconButton(
					onClick = { showDropDownMenu = true },
				) {
					Icon(
						painter = Res.drawable.options.getPainter(),
						contentDescription = Res.string.options.getString(),
						tint = textColor,
					)
				}
				DropdownMenu(
					expanded = showDropDownMenu,
					onDismissRequest = { showDropDownMenu = false },
				) {
					if (!hostedSyncGroup.isDefault) {
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
					DropdownMenuItem(
						text = { Text(Res.string.edit.getString()) },
						onClick = {
							showEditDialog = true
							showDropDownMenu = false
						},
						trailingIcon = {
							Icon(
								painter = Res.drawable.edit.getPainter(),
								contentDescription = Res.string.edit.getString(),
							)
						},
					)
				}
			}
		}
	}

	ConfirmationDialog(
		visible = showDeletionDialog,
		title = Res.string.confirm_action.getString(),
		message = Res.string.delete_group.getString(hostedSyncGroup.name),
		onConfirm = { ServerIntent.DeleteGroup(hostedSyncGroup).execute() },
		onDismiss = { showDeletionDialog = false },
		confirmText = Res.string.delete.getString(),
	)

	InputDialog(
		visible = showEditDialog,
		title = Res.string.edit_group_name.getString(),
		onConfirm = { ServerIntent.EditGroupName(it, hostedSyncGroup).execute() },
		initialValue = hostedSyncGroup.name,
		onDismiss = {
			showEditDialog = false
			focusManager.clearFocus()
		},
		confirmText = Res.string.edit.getString(),
		keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
	)

}
