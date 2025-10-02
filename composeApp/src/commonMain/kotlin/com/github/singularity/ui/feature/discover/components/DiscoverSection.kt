package com.github.singularity.ui.feature.discover.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.feature.discover.DiscoverIntent
import com.github.singularity.ui.feature.discover.DiscoverUiState
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.approved_to_join
import singularity.composeapp.generated.resources.await_pair_request_approval
import singularity.composeapp.generated.resources.refresh


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.DiscoverSection(
    uiState: DiscoverUiState,
    execute: DiscoverIntent.() -> Unit,
) {
    AnimatedContent(
        targetState = uiState.sentPairRequestState,
    ) {
        if (it !is PairRequestState.Idle) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                when (uiState.sentPairRequestState) {
                    is PairRequestState.Awaiting -> {
                        Text(Res.string.await_pair_request_approval.getString(uiState.sentPairRequestState.server.syncGroupName))
                        CircularProgressIndicator()
                    }

                    is PairRequestState.Success -> Text(
                        Res.string.approved_to_join.getString(
                            uiState.sentPairRequestState.server
                        )
                    )

                    is PairRequestState.Error -> Text(uiState.sentPairRequestState.message)
                    is PairRequestState.Idle -> Unit
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier.weight(0.8f)
            .fillMaxWidth(),
    ) {
        items(uiState.availableServers) {
            ServerItem(
                server = it,
                execute = execute,
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth().weight(0.2f),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = { DiscoverIntent.RefreshDiscovery.execute() },
        ) {
            Text(Res.string.refresh.getString())
        }
    }

}
