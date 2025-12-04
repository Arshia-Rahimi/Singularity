package com.github.singularity.core.datasource.network.impl

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.datasource.database.HostedSyncGroupNodeModel
import com.github.singularity.core.datasource.network.AuthTokenDataSource
import com.github.singularity.core.datasource.network.NodeDto
import kotlinx.coroutines.flow.first
import kotlin.random.Random

class RandomAuthTokenDataSource(
    private val hostedSyncGroupRepo: HostedSyncGroupRepository,
) : AuthTokenDataSource {

    override suspend fun generateAuthToken(node: NodeDto): HostedSyncGroupNodeModel? {
        val defaultGroup = hostedSyncGroupRepo.defaultSyncGroup.first() ?: return null

        val token = Random.nextLong(1000000000000000000L, Long.MAX_VALUE).toString()
		val hostedNode = HostedSyncGroupNodeModel(
            deviceId = node.deviceId,
            deviceOs = node.deviceOs,
            deviceName = node.deviceName,
            syncGroupId = defaultGroup.hostedSyncGroupId,
            syncGroupName = defaultGroup.name,
            authToken = token,
        )
        hostedSyncGroupRepo.upsert(hostedNode)

        return hostedNode
    }

	override suspend fun getNode(token: String): HostedSyncGroupNodeModel? {
        val defaultGroup = hostedSyncGroupRepo.defaultSyncGroup.first() ?: return null
        return defaultGroup.nodes.firstOrNull { it.authToken == token }
    }

}
