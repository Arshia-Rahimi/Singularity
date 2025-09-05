package com.github.singularity.core.data.impl

import com.github.singularity.core.data.AuthRepository
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.Token
import com.github.singularity.core.server.crypto.AuthTokenRepository
import com.github.singularity.core.shared.model.HostedSyncGroupNode
import com.github.singularity.core.shared.model.http.PairRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class LocalServerAuthRepository(
    private val hostedSyncGroupRepo: HostedSyncGroupRepository,
    private val authTokenRepo: AuthTokenRepository,
    scope: CoroutineScope,
) : AuthRepository {

    private val defaultSyncGroup = hostedSyncGroupRepo.syncGroups
        .map { it.firstOrNull { group -> group.isDefault } }
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), null)

    override suspend fun getNode(token: Token): HostedSyncGroupNode? {
        val defaultGroup = defaultSyncGroup.value ?: return null
        val nodeId = authTokenRepo.getNodeId(token, defaultGroup.hostedSyncGroupId)
        return defaultSyncGroup.value?.nodes?.firstOrNull { it.nodeId == nodeId }
    }

    override suspend fun authenticate(pairRequest: PairRequest): HostedSyncGroupNode? {
        val defaultGroup = defaultSyncGroup.value ?: return null
        if (pairRequest.nodeId != defaultGroup.hostedSyncGroupId) return null

        val isApproved = false // todo

        if (!isApproved) return null

        val authToken =
            authTokenRepo.generateAuthToken(pairRequest.nodeId, defaultGroup.hostedSyncGroupId)

        return HostedSyncGroupNode(
            nodeId = pairRequest.nodeId,
            nodeOs = pairRequest.nodeOs,
            nodeName = pairRequest.nodeName,
            authToken = authToken,
            syncGroupId = defaultGroup.hostedSyncGroupId,
            syncGroupName = defaultGroup.name,
        ).also {
            hostedSyncGroupRepo.create(it)
        }
    }

}
