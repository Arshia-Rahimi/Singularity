package com.github.singularity.core.server.auth

import com.github.singularity.core.data.Token

interface AuthTokenRepository {

    suspend fun generateAuthToken(nodeId: String, defaultGroupId: String): Token

    suspend fun getNodeId(token: Token, defaultGroupId: String): String?

}
