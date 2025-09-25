package com.github.singularity.ui.feature.main.components.broadcast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.core.shared.compose.onCondition
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.ui.designsystem.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.designsystem.components.dialogs.InputDialog
import com.github.singularity.ui.feature.main.MainIntent
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.confirm_action
import singularity.composeapp.generated.resources.delete
import singularity.composeapp.generated.resources.delete_group
import singularity.composeapp.generated.resources.edit
import singularity.composeapp.generated.resources.edit_group_name
import singularity.composeapp.generated.resources.nodes_paired
import singularity.composeapp.generated.resources.options
import singularity.composeapp.generated.resources.set_group_as_default

@Composable
fun LazyItemScope.HostedSyncGroupItem(
    hostedSyncGroup: HostedSyncGroup,
    modifier: Modifier = Modifier,
    execute: MainIntent.BroadcastIntent.() -> Unit,
) {
    var showSetAsDefaultDialog by remember { mutableStateOf(false) }
    var showDropDownMenu by remember { mutableStateOf(false) }
    var showDeletionDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier.fillMaxWidth()
            .animateItem()
            .onCondition(hostedSyncGroup.isDefault) { background(MaterialTheme.colorScheme.secondaryContainer) }
            .padding(vertical = 4.dp, horizontal = 8.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                fontSize = 16.sp,
                text = hostedSyncGroup.name,
                color = if (hostedSyncGroup.isDefault) MaterialTheme.colorScheme.onSecondaryContainer else Color.Unspecified,
            )

            Text(
                text = "${hostedSyncGroup.nodes.size} ${Res.string.nodes_paired.getString()}",
                fontSize = 12.sp,
                color = if (hostedSyncGroup.isDefault) MaterialTheme.colorScheme.onSecondaryContainer else Color.Unspecified,
            )
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

    ConfirmationDialog(
        visible = showSetAsDefaultDialog,
        message = Res.string.set_group_as_default.getString(hostedSyncGroup.name),
        onConfirm = { MainIntent.BroadcastIntent.SetAsDefault(hostedSyncGroup).execute() },
        onDismiss = { showSetAsDefaultDialog = false },
    )

    ConfirmationDialog(
        visible = showDeletionDialog,
        title = Res.string.confirm_action.getString(),
        message = Res.string.delete_group.getString(hostedSyncGroup.name),
        onConfirm = { MainIntent.BroadcastIntent.DeleteGroup(hostedSyncGroup).execute() },
        onDismiss = { showDeletionDialog = false },
        confirmText = Res.string.delete.getString(),
    )

    InputDialog(
        visible = showEditDialog,
        title = Res.string.edit_group_name.getString(),
        onConfirm = { MainIntent.BroadcastIntent.EditGroupName(it, hostedSyncGroup).execute() },
        initialValue = hostedSyncGroup.name,
        onDismiss = {
            showEditDialog = false
            focusManager.clearFocus()
        },
        confirmText = Res.string.edit.getString(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    )

}
