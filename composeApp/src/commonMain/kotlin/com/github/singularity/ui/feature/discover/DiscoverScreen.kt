package com.github.singularity.ui.feature.discover

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.compose.getString
import com.github.singularity.ui.feature.discover.components.PairRequestState
import com.github.singularity.ui.feature.discover.components.ServerItem
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.arrow_back
import singularity.composeapp.generated.resources.await_pair_request_approval
import singularity.composeapp.generated.resources.back
import singularity.composeapp.generated.resources.discover

@Composable
fun DiscoverScreen(
    navBack: () -> Unit,
) {
    val viewModel = koinViewModel<DiscoverViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DiscoverScreen(
        uiState = uiState,
        execute = {
            when (this) {
                is DiscoverIntent.NavBack -> navBack()
                else -> viewModel.execute(this)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverScreen(
    uiState: DiscoverUiState,
    execute: DiscoverIntent.() -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(Res.string.discover.getString()) },
                navigationIcon = {
                    IconButton(
                        onClick = { DiscoverIntent.NavBack.execute() },
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.arrow_back),
                            contentDescription = Res.string.back.getString(),
                        )
                    }
                },
            )
        },
    ) { ip ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(ip)
                .padding(4.dp),
        ) {
            AnimatedContent(
                targetState = uiState.pairRequestState,
            ) {
                if (it !is PairRequestState.Idle) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        when (uiState.pairRequestState) {
                            is PairRequestState.Awaiting -> {
                                Text(Res.string.await_pair_request_approval.getString(uiState.pairRequestState.server.syncGroupName))
                                CircularProgressIndicator()
                            }

                            is PairRequestState.Success -> Text(Res.string.await_pair_request_approval.toString())
                            is PairRequestState.Error -> Text(uiState.pairRequestState.message)
                            is PairRequestState.Idle -> Unit
                        }
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.weight(1f)
                    .fillMaxWidth(),
            ) {
                items(uiState.servers) {
                    ServerItem(
                        server = it,
                        execute = execute,
                    )
                }
            }
        }
    }
}
