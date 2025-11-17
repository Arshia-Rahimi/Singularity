package com.github.singularity.core.data.impl

import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.JoinedSyncGroupRepository
import com.github.singularity.core.log.Logger
import com.github.singularity.core.presence.DeviceDiscoveryService
import com.github.singularity.core.shared.DISCOVER_TIMEOUT
import com.github.singularity.core.shared.model.ClientConnectionState
import com.github.singularity.core.shared.model.ClientSyncState
import com.github.singularity.core.shared.util.Resource
import com.github.singularity.core.shared.util.sendPulse
import com.github.singularity.core.sync.datasource.SyncRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
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
                    emit(
                        ClientSyncState.WithDefaultServer(
                            defaultServer,
                            ClientConnectionState.Searching
                        )
                    )

                    val server = withTimeoutOrNull(DISCOVER_TIMEOUT) {
                        deviceDiscoveryService.discoverServer(defaultServer)
                    }

                    if (server == null) {
                        emit(
                            ClientSyncState.WithDefaultServer(
                                defaultServer,
                                ClientConnectionState.ServerNotFound("timeout"),
                            )
                        )
                        logger.i(this::class.simpleName, "server not found")
                        return@flow
                    }

                    syncRemoteDataSource.connect(server, defaultServer.authToken).collect {
                        when (it) {
                            is Resource.Loading -> Unit
                            is Resource.Error -> {
                                logger.e(
                                    this::class.simpleName,
                                    "error connecting to server",
                                    it.error
                                )
                                emit(
                                    ClientSyncState.WithDefaultServer(
                                        defaultServer,
                                        ClientConnectionState.SyncFailed(server),
                                    )
                                )
                            }

                            is Resource.Success -> emit(
                                ClientSyncState.WithDefaultServer(
                                    defaultServer,
                                    ClientConnectionState.Connected(server),
                                )
                            )
                        }
                    }
                }
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun refresh() {
        refreshState.sendPulse()
    }

}
