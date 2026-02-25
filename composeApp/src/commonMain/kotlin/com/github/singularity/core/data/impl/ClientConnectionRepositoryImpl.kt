package com.github.singularity.core.data.impl

import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.JoinedSyncGroupRepository
import com.github.singularity.core.datasource.database.JoinedSyncGroupModel
import com.github.singularity.core.datasource.network.DeviceDiscoveryService
import com.github.singularity.core.datasource.network.LocalServerDto
import com.github.singularity.core.datasource.network.SyncRemoteDataSource
import com.github.singularity.core.log.Logger
import com.github.singularity.core.shared.DISCOVER_TIMEOUT_MS
import com.github.singularity.core.shared.WEBSOCKET_CONNECTION_RETRY_MS
import com.github.singularity.core.shared.WEBSOCKET_MAX_CONNECTION_RETRY_COUNT
import com.github.singularity.core.shared.util.onFirst
import com.github.singularity.core.shared.util.sendPulse
import com.github.singularity.core.syncservice.ClientSyncState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.cancellation.CancellationException


@OptIn(ExperimentalCoroutinesApi::class)
class ClientConnectionRepositoryImpl(
    private val syncRemoteDataSource: SyncRemoteDataSource,
    private val deviceDiscoveryService: DeviceDiscoveryService,
    private val logger: Logger,
    joinedSyncGroupRepo: JoinedSyncGroupRepository,
) : ClientConnectionRepository {

    private val refreshState = MutableSharedFlow<Unit>()

    override suspend fun refresh() {
        refreshState.sendPulse()
    }

    override val connectionState = refreshState
        .onStart { emit(Unit) }
        .flatMapLatest {
            joinedSyncGroupRepo.defaultJoinedSyncGroup.flatMapLatest { defaultServer ->
                if (defaultServer == null) flowOf<ClientSyncState>(ClientSyncState.NoDefaultServer)
                else searchAndConnectTo(defaultServer)
            }
        }.flowOn(Dispatchers.IO)

    private fun searchAndConnectTo(defaultServer: JoinedSyncGroupModel) = flow {
        var tryCounter = 0
        var discoveredServer: LocalServerDto? = null

        while (true) {
            if (tryCounter % WEBSOCKET_MAX_CONNECTION_RETRY_COUNT == 0) {
                emit(ClientSyncState.Searching(defaultServer.syncGroupName))
                discoveredServer = withTimeoutOrNull(DISCOVER_TIMEOUT_MS) {
                    deviceDiscoveryService.findServer(defaultServer)
                }
            }

            if (discoveredServer == null) {
                emit(ClientSyncState.ServerNotFound(defaultServer.syncGroupName, "timeout"))
                logger.e("server not found")
                tryCounter = 0
            } else {
                try {
                    syncRemoteDataSource.connect(discoveredServer, defaultServer.authToken)
                        .onFirst { tryCounter = 0 }
                        .collect {
                            emit(ClientSyncState.Connected(defaultServer.syncGroupName))
                        }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    logger.e("error connecting to server", e)
                    emit(ClientSyncState.ConnectionFailed(defaultServer.syncGroupName))
                }
            }

            delay(WEBSOCKET_CONNECTION_RETRY_MS)
            tryCounter++
        }
    }

}
