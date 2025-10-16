package com.github.singularity.core.data.impl

import com.github.singularity.core.broadcast.DeviceDiscoveryService
import com.github.singularity.core.client.PairRemoteDataSource
import com.github.singularity.core.data.DiscoverRepository
import com.github.singularity.core.data.JoinedSyncGroupRepository
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.PAIR_CHECK_RETRY_DELAY
import com.github.singularity.core.shared.deviceName
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.http.PairStatus
import com.github.singularity.core.shared.os
import com.github.singularity.core.shared.util.Success
import com.github.singularity.core.shared.util.asResult
import com.github.singularity.core.shared.util.sendPulse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class DiscoverRepositoryImp(
	private val joinedSyncGroupsRepo: JoinedSyncGroupRepository,
	private val preferencesRepo: PreferencesRepository,
	private val pairRemoteDataSource: PairRemoteDataSource,
	private val discoveryService: DeviceDiscoveryService,
) : DiscoverRepository {

    private val refreshState = MutableSharedFlow<Unit>()

    override val discoveredServers = refreshState
        .onStart { emit(Unit) }
        .flatMapLatest {
            discoveryService.discoveredServers()
                .onStart { emit(emptyList()) }
                .catch {}
        }

    override suspend fun refreshDiscovery() {
        refreshState.sendPulse()
    }

    override fun sendPairRequest(server: LocalServer) = flow {
        try {
            val response = pairRemoteDataSource.sendPairRequest(server, getCurrentDeviceAsNode())
            if (!response.success || response.pairRequestId == null) {
                throw Exception("failed to connect")
            }

            var isWaiting = true
            while (isWaiting) {
                delay(PAIR_CHECK_RETRY_DELAY)

                val response = pairRemoteDataSource
                    .sendPairCheckRequest(server, response.pairRequestId)

                if (response.pairStatus == PairStatus.Awaiting) continue

                isWaiting = false

                when (response.pairStatus) {
                    PairStatus.Approved -> {
                        val newGroup = JoinedSyncGroup(
                            syncGroupId = response.node?.syncGroupId ?: "",
                            syncGroupName = response.node?.syncGroupName ?: "",
                            authToken = response.node?.authToken ?: "",
                        )
                        joinedSyncGroupsRepo.upsert(newGroup)
                        joinedSyncGroupsRepo.setAsDefault(newGroup)

                        emit(Success)
                    }

                    PairStatus.Rejected -> {
                        throw Exception("server rejected pair request")
                    }

                    else -> Unit
                }

            }

        } catch (e: IOException) {
            throw Exception("failed to send pair request: ${e.message}")
        }
    }.asResult(Dispatchers.IO)

    private suspend fun getCurrentDeviceAsNode() = Node(
        deviceName = deviceName,
        deviceOs = os,
        deviceId = preferencesRepo.preferences.first().deviceId,
    )

}
