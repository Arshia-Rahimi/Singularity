package com.github.singularity.core.data

import com.github.singularity.core.database.entities.HostedSyncGroupNode
import com.github.singularity.core.shared.model.Node
import io.ktor.server.auth.BearerTokenCredential

interface AuthRepository {

    suspend fun getNode(token: BearerTokenCredential): HostedSyncGroupNode?

    suspend fun authenticate(node: Node): String

}
