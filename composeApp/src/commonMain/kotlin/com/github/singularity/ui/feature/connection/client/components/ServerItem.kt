package com.github.singularity.ui.feature.connection.client.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.ui.designsystem.components.dialogs.ConfirmationDialog
import com.github.singularity.ui.feature.connection.client.ClientIntent
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.send_pair_request

@Composable
fun LazyGridItemScope.ServerItem(
    server: LocalServer,
    execute: ClientIntent.() -> Unit,
) {
    var showPairRequestDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .animateItem()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {

    }

    ConfirmationDialog(
        visible = showPairRequestDialog,
        message = Res.string.send_pair_request.getString(server.syncGroupName),
        onConfirm = { ClientIntent.SendPairRequest(server).execute() },
        onDismiss = { showPairRequestDialog = false },
    )

}
