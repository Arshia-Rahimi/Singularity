package com.github.singularity.core.data.impl

import com.github.singularity.core.data.AuthTokenRepository
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.Token
import com.github.singularity.core.shared.model.HostedSyncGroupNode
import com.github.singularity.core.shared.model.Node
import kotlinx.coroutines.flow.first
import kotlin.random.Random

class RandomTokenAuthRepository(
    private val hostedSyncGroupRepo: HostedSyncGroupRepository,
) : AuthTokenRepository {

    override suspend fun generateAuthToken(node: Node): HostedSyncGroupNode? {
        val defaultGroup = hostedSyncGroupRepo.defaultSyncGroup.first() ?: return null

        val token = Random.nextLong(1000000000000000000L, Long.MAX_VALUE).toString()
        val hostedNode = HostedSyncGroupNode(
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

    override suspend fun getNode(token: Token): HostedSyncGroupNode? {
        val defaultGroup = hostedSyncGroupRepo.defaultSyncGroup.first() ?: return null
        return defaultGroup.nodes.firstOrNull { it.authToken == token }
    }

}
