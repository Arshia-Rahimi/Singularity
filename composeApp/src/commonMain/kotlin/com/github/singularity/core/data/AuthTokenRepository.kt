package com.github.singularity.core.data

import com.github.singularity.core.shared.model.HostedSyncGroupNode
import com.github.singularity.core.shared.model.Node

typealias Token = String

interface AuthTokenRepository {

    suspend fun generateAuthToken(node: Node): HostedSyncGroupNode?

    suspend fun getNode(token: Token): HostedSyncGroupNode?

}
