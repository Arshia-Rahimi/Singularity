package com.github.singularity.core.data.impl

import com.github.singularity.core.client.KtorHttpClient
import com.github.singularity.core.data.DiscoverRepository
import com.github.singularity.core.database.LocalJoinedSyncGroupsDataSource
import com.github.singularity.core.database.entities.JoinedSyncGroup
import com.github.singularity.core.mdns.DeviceDiscoveryService
import com.github.singularity.core.mdns.Server
import com.github.singularity.core.shared.util.Success
import com.github.singularity.core.shared.util.asResult
import com.github.singularity.models.PairRequestResponse
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flow

class DiscoverRepositoryImp(
    discoveryService: DeviceDiscoveryService,
    private val joinedSyncGroupsRepo: LocalJoinedSyncGroupsDataSource,
    private val client: KtorHttpClient,
) : DiscoverRepository {

    override val discoveredServers = discoveryService.discoverServers()

    override fun sendPairRequest(server: Server) = flow {
        val httpResponse = try {
            client.sendPairRequest(server)
        } catch (_: Exception) {
            throw Exception("failed to send pair request")
        }
        val statusCode = httpResponse.status
        val response = httpResponse.body<PairRequestResponse>()

        when (statusCode) {
            HttpStatusCode.OK -> {
                val newGroup = JoinedSyncGroup(
                    joinedSyncGroupId = server.syncGroupId,
                    name = server.syncGroupName,
                    authToken = response.authToken ?: "",
                )
                joinedSyncGroupsRepo.insert(newGroup)
                joinedSyncGroupsRepo.setAsDefault(newGroup)
                emit(Success)
            }

            else -> throw Exception(response.message)
        }

    }.asResult(Dispatchers.IO)

    override fun release() {
        client.release()
    }

}
