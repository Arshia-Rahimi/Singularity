package com.github.singularity.core.data.impl

import com.github.singularity.core.broadcast.DeviceBroadcastService
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.server.KtorHttpServer
import com.github.singularity.core.server.KtorWebSocketServer
import com.github.singularity.core.server.PairRequestDataSource
import com.github.singularity.core.shared.model.ServerConnectionState
import com.github.singularity.core.shared.model.http.PairStatus
import com.github.singularity.core.shared.util.sendPulse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

@OptIn(ExperimentalCoroutinesApi::class)
class ServerConnectionRepositoryImpl(
	private val webSocketServer: KtorWebSocketServer,
	private val hostedSyncGroupRepo: HostedSyncGroupRepository,
	private val pairRequestDataSource: PairRequestDataSource,
	private val broadcastService: DeviceBroadcastService,
	private val httpServer: KtorHttpServer,
) : ServerConnectionRepository {

	private val refreshState = MutableSharedFlow<Unit>()

	override fun runServer() = refreshState
		.onStart { emit(Unit) }
		.flatMapLatest {
			hostedSyncGroupRepo.defaultSyncGroup
				.flatMapLatest { group ->
					if (group == null) flowOf(ServerConnectionState.NoDefaultServer)
					else {
						webSocketServer.start()
						httpServer.start(group)
						broadcastService.startBroadcast(group)

						combine(
							webSocketServer.connectedNodes,
							pairRequestDataSource.requests,
						) { connectedNodes, requests ->
							ServerConnectionState.Running(
								group = group,
								connectedNodes = connectedNodes,
								pairRequests = requests.filter { it.status == PairStatus.Awaiting }
									.map { it.node }
							)
						}
					}
				}
		}.onCompletion {
			httpServer.stop()
			webSocketServer.stop()
			broadcastService.stopBroadcast()
			pairRequestDataSource.clear()
		}.flowOn(Dispatchers.IO)

	override suspend fun refresh() {
		refreshState.sendPulse()
	}

}
