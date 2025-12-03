package com.github.singularity.ui.feature.connection.client.pages.index

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.singularity.ui.designsystem.shared.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.designsystem.shared.getString
import com.github.singularity.ui.feature.connection.client.ClientIntent
import com.github.singularity.ui.feature.connection.client.DiscoveredServer
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.send_pair_request

@Composable
fun LazyGridItemScope.ServerItem(
	server: DiscoveredServer,
	execute: ClientIntent.() -> Unit,
) {
    var showPairRequestDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .animateItem()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
	        .clickable { showPairRequestDialog = true }
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
	            text = server.groupName,
                fontSize = 16.sp,
            )
            Text(
                text = "${server.deviceName} - ${server.deviceOs}",
                fontSize = 12.sp,
            )
            Text(
                text = server.ip,
                fontSize = 12.sp,
            )
        }
    }

    ConfirmationDialog(
        visible = showPairRequestDialog,
	    message = Res.string.send_pair_request.getString(server.groupName),
        onConfirm = { ClientIntent.SendPairRequest(server).execute() },
        onDismiss = { showPairRequestDialog = false },
    )

}
