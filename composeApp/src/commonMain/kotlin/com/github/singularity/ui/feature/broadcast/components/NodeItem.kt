package com.github.singularity.ui.feature.broadcast.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.core.shared.model.Node
import com.github.singularity.ui.designsystem.components.dialogs.ApprovalDialog
import com.github.singularity.ui.feature.broadcast.BroadcastIntent
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.approve_pair_request
import singularity.composeapp.generated.resources.reject_or_approve

@Composable
fun NodeItem(
    node: Node,
    execute: BroadcastIntent.() -> Unit,
) {
    var showApproveNodeDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = node.deviceName,
            fontSize = 16.sp,
        )
        Text(
            text = node.deviceOs,
            fontSize = 12.sp,
        )
    }

    ApprovalDialog(
        visible = showApproveNodeDialog,
        title = Res.string.approve_pair_request.getString(),
        message = Res.string.reject_or_approve.getString(),
        onApprove = { BroadcastIntent.Approve(node).execute() },
        onReject = { BroadcastIntent.Reject(node).execute() },
        onDismiss = { showApproveNodeDialog = false },
    )

}
