package com.github.singularity.ui.feature.connection.client

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.ui.designsystem.components.PulseAnimation
import com.github.singularity.ui.designsystem.components.TopBar
import com.github.singularity.ui.designsystem.shared.getPainter
import com.github.singularity.ui.designsystem.shared.getString
import com.github.singularity.ui.feature.connection.client.components.JoinedSyncGroupItem
import com.github.singularity.ui.feature.connection.client.components.PairRequestState
import com.github.singularity.ui.feature.connection.client.components.ServerItem
import org.koin.compose.viewmodel.koinViewModel
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.available_servers
import singularity.composeapp.generated.resources.await_pair_request_approval
import singularity.composeapp.generated.resources.discover
import singularity.composeapp.generated.resources.joined_sync_groups
import singularity.composeapp.generated.resources.plus
import singularity.composeapp.generated.resources.rejected_pair_request_approval
import singularity.composeapp.generated.resources.searching_dotted
import singularity.composeapp.generated.resources.stop

@Composable
fun ClientScreen() {
	val viewModel = koinViewModel<ClientViewModel>()
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	ClientScreen(
		uiState = uiState,
		execute = { viewModel.execute(this) },
	)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClientScreen(
	uiState: ClientUiState,
	execute: ClientIntent.() -> Unit,
) {

	Scaffold(
		topBar = {
			TopBar(
				title = Res.string.discover.getString(),
			)
		},
	) { ip ->

		LazyVerticalGrid(
			columns = GridCells.Adaptive(200.dp),
			modifier = Modifier.fillMaxSize()
				.padding(ip)
				.padding(horizontal = 4.dp),
		) {

			val pairRequestState = uiState.sentPairRequestState
			if (pairRequestState != PairRequestState.Idle) {
				stickyHeader(
					key = "sent_pair_request_title",
				) {
					when (pairRequestState) {
						is PairRequestState.Awaiting -> Text(
							Res.string.await_pair_request_approval.getString(
								pairRequestState.server.syncGroupName
							)
						)

						is PairRequestState.Error -> Text(
							Res.string.rejected_pair_request_approval.getString(
								pairRequestState.message
							)
						)

						else -> Unit
					}
				}
			}

			if (!uiState.joinedSyncGroups.isEmpty()) {
				stickyHeader(
					key = "joined_title",
					contentType = "title",
				) {
					Row(
						modifier = Modifier
							.animateItem()
							.fillMaxWidth()
							.padding(vertical = 8.dp, horizontal = 12.dp),
					) {
						Text(
							text = Res.string.joined_sync_groups.getString(),
							fontSize = 20.sp,
						)
					}
				}

				items(
					items = uiState.joinedSyncGroups,
					key = { it.syncGroupId },
					contentType = { it },
				) {
					JoinedSyncGroupItem(
						joinedSyncGroup = it,
						execute = execute,
					)
				}
			}

			if (uiState.isDiscovering) {
				stickyHeader(
					key = "available_title",
					contentType = "title",
				) {
					Row(
						modifier = Modifier
							.animateItem()
							.fillMaxWidth()
							.padding(vertical = 8.dp, horizontal = 12.dp),
					) {
						Text(
							text = Res.string.available_servers.getString(),
							fontSize = 20.sp,
						)
					}
				}
			} else {
				stickyHeader(
					key = "discover_title",
					contentType = "title",
				) {
					Row(
						modifier = Modifier
							.animateItem()
							.fillMaxWidth()
							.padding(vertical = 8.dp),
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.Center,
					) {
						IconButton(
							onClick = { ClientIntent.StartDiscovery.execute() },
						) {
							Icon(
								painter = Res.drawable.plus.getPainter(),
								contentDescription = "discover",
							)
						}
					}
				}
			}

			items(
				items = uiState.availableServers,
				key = { it.syncGroupId },
				contentType = { it },
			) {
				ServerItem(
					server = it,
					execute = execute,
				)
			}

			if (uiState.isDiscovering) {
				stickyHeader(
					key = "pulse_animation",
					contentType = "animation",
				) {
					Column(
						modifier = Modifier
							.animateItem()
							.fillMaxWidth()
							.padding(vertical = 8.dp),
						horizontalAlignment = Alignment.CenterHorizontally,
					) {
						PulseAnimation(
							modifier = Modifier.size(150.dp)
						) {
							Text(
								text = Res.string.searching_dotted.getString(),
							)
						}

						Row(
							modifier = Modifier.fillMaxWidth(),
							horizontalArrangement = Arrangement.SpaceEvenly,
						) {
							TextButton(
								onClick = { ClientIntent.StopDiscovery.execute() },
							) {
								Text(
									text = Res.string.stop.getString(),
								)
							}
						}
					}
				}
			}

		}

	}
}
