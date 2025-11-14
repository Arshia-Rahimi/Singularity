package com.github.singularity.ui.feature.connection.server.pages.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.core.shared.model.Node
import com.github.singularity.ui.designsystem.shared.components.dialogs.ApprovalDialog
import com.github.singularity.ui.designsystem.shared.getString
import com.github.singularity.ui.feature.connection.server.ServerIntent
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.approve_pair_request
import singularity.composeapp.generated.resources.reject_or_approve

@Composable
fun LazyGridItemScope.NodeItem(
	isPairRequest: Boolean = false,
	node: Node,
	execute: ServerIntent.() -> Unit,
) {
    var showApproveNodeDialog by remember { mutableStateOf(false) }

	val containerColor = MaterialTheme.colorScheme.secondaryContainer
	val textColor = MaterialTheme.colorScheme.onSecondaryContainer

    Box(
        modifier = Modifier
            .animateItem()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
	        .background(containerColor)
	        .clickable(enabled = isPairRequest) { showApproveNodeDialog = true }
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
	            fontSize = 12.sp,
                text = node.deviceName,
	            color = textColor,
            )

            Text(
                text = node.deviceOs,
	            fontSize = 10.sp,
	            color = textColor,
            )
        }
    }

    ApprovalDialog(
        visible = showApproveNodeDialog,
        title = Res.string.approve_pair_request.getString(),
        message = Res.string.reject_or_approve.getString(node.deviceName),
        onApprove = { ServerIntent.Approve(node).execute() },
        onReject = { ServerIntent.Reject(node).execute() },
        onDismiss = { showApproveNodeDialog = false },
    )

}
