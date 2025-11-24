package com.github.singularity.core.datasource.memory

import com.github.singularity.core.shared.model.HostedSyncGroupNode
import com.github.singularity.core.shared.model.Node

interface AuthTokenDataSource {

    suspend fun generateAuthToken(node: Node): HostedSyncGroupNode?

    suspend fun getNode(token: String): HostedSyncGroupNode?

}
