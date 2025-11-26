package com.github.singularity.core.data.impl

import com.github.singularity.core.data.DiscoverRepository
import com.github.singularity.core.data.JoinedSyncGroupRepository
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.datasource.network.SyncRemoteDataSource
import com.github.singularity.core.datasource.presence.DeviceDiscoveryService
import com.github.singularity.core.log.Logger
import com.github.singularity.core.shared.PAIR_CHECK_RETRY_MS
import com.github.singularity.core.shared.deviceName
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.http.PairStatus
import com.github.singularity.core.shared.os
import com.github.singularity.core.shared.util.Success
import com.github.singularity.core.shared.util.asResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class DiscoverRepositoryImpl(
    private val joinedSyncGroupsRepo: JoinedSyncGroupRepository,
    private val preferencesRepo: PreferencesRepository,
    private val syncRemoteDataSource: SyncRemoteDataSource,
    private val logger: Logger,
    discoveryService: DeviceDiscoveryService,
) : DiscoverRepository {

	override val discoveredServers = discoveryService.discoveredServers()
		.onStart { emit(emptyList()) }
		.catch {}
		.flowOn(Dispatchers.IO)

	override fun sendPairRequest(server: LocalServer) = flow {
		try {
            val response = syncRemoteDataSource.sendPairRequest(server, getCurrentDeviceAsNode())
			if (!response.success || response.pairRequestId == null) {
				throw Exception("failed to connect")
			}

			var isWaiting = true
			while (isWaiting) {
				delay(PAIR_CHECK_RETRY_MS)

                val response = syncRemoteDataSource
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
			logger.e(this::class.simpleName, "failed to send pair request", e)
			throw Exception("failed to send pair request: ${e.message}")
		}
    }.asResult(Dispatchers.IO)

    override suspend fun removeAllDefaults() {
        joinedSyncGroupsRepo.removeAllDefaults()
    }

    private suspend fun getCurrentDeviceAsNode() = Node(
		deviceName = deviceName,
		deviceOs = os,
		deviceId = preferencesRepo.preferences.first().deviceId,
	)

}
