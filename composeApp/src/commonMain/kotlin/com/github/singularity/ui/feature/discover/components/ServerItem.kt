package com.github.singularity.ui.feature.discover.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.ui.designsystem.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.feature.discover.DiscoverIntent
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.send_pair_request

@Composable
fun ServerItem(
    server: LocalServer,
    execute: DiscoverIntent.() -> Unit,
) {
    var showPairRequestDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
            .clickable { showPairRequestDialog = true }
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        Text(
            text = server.syncGroupName,
            fontSize = 16.sp,
        )
        Text(
            text = server.ip,
            fontSize = 12.sp,
        )
        Text(
            text = "${server.deviceName} ${server.deviceOs}",
            fontSize = 12.sp,
        )
    }

    ConfirmationDialog(
        visible = showPairRequestDialog,
        message = Res.string.send_pair_request.getString(server.syncGroupName),
        onConfirm = { DiscoverIntent.SendPairRequest(server).execute() },
        onDismiss = { showPairRequestDialog = false },
    )

}
