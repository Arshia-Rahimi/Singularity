package com.github.singularity.core.data.impl

import com.github.singularity.core.client.HttpClientDataSource
import com.github.singularity.core.data.DiscoverRepository
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.database.JoinedSyncGroupDataSource
import com.github.singularity.core.database.entities.JoinedSyncGroup
import com.github.singularity.core.mdns.DeviceDiscoveryService
import com.github.singularity.core.shared.getDeviceName
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.os
import com.github.singularity.core.shared.util.Success
import com.github.singularity.core.shared.util.asResult
import com.github.singularity.models.Node
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okio.IOException

class DiscoverRepositoryImp(
    discoveryService: DeviceDiscoveryService,
    private val joinedSyncGroupsRepo: JoinedSyncGroupDataSource,
    private val preferencesRepo: PreferencesRepository,
    private val httpClientDataSource: HttpClientDataSource,
) : DiscoverRepository {

    override val discoveredServers = discoveryService.discoverServers()

    override fun sendPairRequest(server: LocalServer) = flow {
        try {
            val response = httpClientDataSource.sendPairRequest(server, getCurrentDeviceAsNode())
            val newGroup = JoinedSyncGroup(
                syncGroupId = server.syncGroupId,
                syncGroupName = server.syncGroupName,
                authToken = response.authToken ?: "",
                ip = server.ip,
                isLocal = true,
            )
            joinedSyncGroupsRepo.insert(newGroup)
            joinedSyncGroupsRepo.setAsDefault(newGroup)
            emit(Success)
        } catch (_: IOException) {
            throw Exception("failed to send pair request")
        }
    }.asResult(Dispatchers.IO)

    override fun release() {
        httpClientDataSource.release()
    }

    private suspend fun getCurrentDeviceAsNode() = Node(
        deviceName = getDeviceName(),
        deviceOs = os,
        deviceId = preferencesRepo.preferences.first().deviceId,
        ip = "",
    )

}
