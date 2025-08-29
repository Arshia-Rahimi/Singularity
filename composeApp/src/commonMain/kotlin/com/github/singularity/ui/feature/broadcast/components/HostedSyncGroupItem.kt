package com.github.singularity.ui.feature.broadcast.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.core.database.entities.HostedSyncGroup
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.designsystem.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.feature.broadcast.BroadcastIntent
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.confirm_action
import singularity.composeapp.generated.resources.default
import singularity.composeapp.generated.resources.delete
import singularity.composeapp.generated.resources.delete_group
import singularity.composeapp.generated.resources.nodes_paired
import singularity.composeapp.generated.resources.options
import singularity.composeapp.generated.resources.set_group_as_default

@Composable
fun LazyItemScope.HostedSyncGroupItem(
    hostedSyncGroup: HostedSyncGroup,
    execute: BroadcastIntent.() -> Unit,
) {
    var showSetAsDefaultDialog by remember { mutableStateOf(false) }
    var showDropDownMenu by remember { mutableStateOf(false) }
    var showDeletionDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth()
            .animateItem()
            .clickable { showSetAsDefaultDialog = true }
            .padding(vertical = 4.dp, horizontal = 8.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    fontSize = 16.sp,
                    text = hostedSyncGroup.name,
                    modifier = Modifier.padding(end = 8.dp),
                )
                if (hostedSyncGroup.isDefault) {
                    Text(
                        fontSize = 16.sp,
                        text = Res.string.default.getString(),
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(4.dp),
                        )
                    )
                }
            }

            Text(
                text = "${hostedSyncGroup.nodes.size} ${Res.string.nodes_paired.getString()}",
                fontSize = 12.sp,
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
                    }
                )
            }
        }

    }

    ConfirmationDialog(
        visible = showSetAsDefaultDialog,
        message = Res.string.set_group_as_default.getString(hostedSyncGroup.name),
        onConfirm = { BroadcastIntent.SetAsDefault(hostedSyncGroup).execute() },
        onDismiss = { showSetAsDefaultDialog = false },
    )

    ConfirmationDialog(
        visible = showDeletionDialog,
        title = Res.string.confirm_action.getString(),
        message = Res.string.delete_group.getString(hostedSyncGroup.name),
        onConfirm = { BroadcastIntent.DeleteGroup(hostedSyncGroup).execute() },
        onDismiss = { showDeletionDialog = false },
        confirmText = Res.string.delete.getString(),
    )
    
}
