package com.github.singularity.ui.feature.discover.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.core.shared.compose.getPainter
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.core.shared.compose.onCondition
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.ui.designsystem.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.feature.discover.DiscoverIntent
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.confirm_action
import singularity.composeapp.generated.resources.delete
import singularity.composeapp.generated.resources.delete_group
import singularity.composeapp.generated.resources.options
import singularity.composeapp.generated.resources.set_group_as_default

@Composable
fun LazyItemScope.JoinedSyncGroupItem(
    joinedSyncGroup: JoinedSyncGroup,
    modifier: Modifier = Modifier,
    execute: DiscoverIntent.() -> Unit,
) {
    var showSetAsDefaultDialog by remember { mutableStateOf(false) }
    var showDropDownMenu by remember { mutableStateOf(false) }
    var showDeletionDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth()
            .animateItem()
            .onCondition(joinedSyncGroup.isDefault) {
                background(MaterialTheme.colorScheme.secondaryContainer)
            }
            .onCondition(!joinedSyncGroup.isDefault) {
                clickable { showSetAsDefaultDialog = true }
            }
            .padding(vertical = 4.dp, horizontal = 8.dp),
    ) {
        Text(
            fontSize = 16.sp,
            text = joinedSyncGroup.syncGroupName,
            color = if (joinedSyncGroup.isDefault) MaterialTheme.colorScheme.onSecondaryContainer else Color.Unspecified,
        )

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
        visible = showSetAsDefaultDialog,
        message = Res.string.set_group_as_default.getString(joinedSyncGroup.syncGroupName),
        onConfirm = { DiscoverIntent.SetAsDefault(joinedSyncGroup).execute() },
        onDismiss = { showSetAsDefaultDialog = false },
    )

    ConfirmationDialog(
        visible = showDeletionDialog,
        title = Res.string.confirm_action.getString(),
        message = Res.string.delete_group.getString(joinedSyncGroup.syncGroupName),
        onConfirm = { DiscoverIntent.DeleteGroup(joinedSyncGroup).execute() },
        onDismiss = { showDeletionDialog = false },
        confirmText = Res.string.delete.getString(),
    )
}
