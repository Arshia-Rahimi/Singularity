package com.github.singularity.core.data.impl

import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.JoinedSyncGroupRepository
import com.github.singularity.core.datasource.network.SyncRemoteDataSource
import com.github.singularity.core.datasource.presence.DeviceDiscoveryService
import com.github.singularity.core.log.Logger
import com.github.singularity.core.shared.DISCOVER_TIMEOUT_MS
import com.github.singularity.core.shared.WEBSOCKET_CONNECTION_RETRY_MS
import com.github.singularity.core.shared.model.ClientSyncState
import com.github.singularity.core.shared.util.sendPulse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withTimeoutOrNull

@OptIn(ExperimentalCoroutinesApi::class)
class ClientConnectionRepositoryImpl(
	private val syncRemoteDataSource: SyncRemoteDataSource,
	joinedSyncGroupRepo: JoinedSyncGroupRepository,
	deviceDiscoveryService: DeviceDiscoveryService,
	logger: Logger,
) : ClientConnectionRepository {

	private val refreshState = MutableSharedFlow<Unit>()

	override val connectionState = refreshState
		.onStart { emit(Unit) }
		.flatMapLatest {
			joinedSyncGroupRepo.defaultJoinedSyncGroup.flatMapLatest { defaultServer ->
				if (defaultServer == null) flowOf<ClientSyncState>(ClientSyncState.NoDefaultServer)
				else flow {
					emit(ClientSyncState.Searching(defaultServer.syncGroupName))

					val server = withTimeoutOrNull(DISCOVER_TIMEOUT_MS) {
						deviceDiscoveryService.discoverServer(defaultServer)
					}

					if (server == null) {
						emit(
							ClientSyncState.ServerNotFound(defaultServer.syncGroupName, "timeout")
						)
						logger.e(this::class.simpleName, "server not found")
						return@flow
					}

					while (true) {
						syncRemoteDataSource.connect(server, defaultServer.authToken)
							.catch {
								logger.e(
									this::class.simpleName,
									"error connecting to server",
									it
								)
								emit(
									ClientSyncState.SyncFailed(
										defaultServer.syncGroupName,
										server,
									)
								)
							}
							.collect {
								emit(
									ClientSyncState.Connected(
										defaultServer.syncGroupName,
										server,
									)
								)
							}
						delay(WEBSOCKET_CONNECTION_RETRY_MS)
					}
				}
			}
		}.flowOn(Dispatchers.IO)

	override suspend fun refresh() {
		refreshState.sendPulse()
	}

}
