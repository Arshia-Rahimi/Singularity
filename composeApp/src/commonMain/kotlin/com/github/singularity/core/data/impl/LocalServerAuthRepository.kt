package com.github.singularity.core.data.impl

import com.github.singularity.core.data.AuthRepository
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.shared.model.Node
import io.ktor.server.auth.BearerTokenCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class LocalServerAuthRepository(
    private val hostedSyncGroupRepo: HostedSyncGroupRepository,
    scope: CoroutineScope,
) : AuthRepository {

    private val defaultSyncGroup = hostedSyncGroupRepo.syncGroups
        .map { it.firstOrNull { group -> group.isDefault } }
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), null)

    override suspend fun getNode(token: BearerTokenCredential) =
        defaultSyncGroup.value?.nodes?.firstOrNull { it.authToken == token.token }


    override suspend fun authenticate(node: Node): String {
        TODO("Not yet implemented")
    }

}
