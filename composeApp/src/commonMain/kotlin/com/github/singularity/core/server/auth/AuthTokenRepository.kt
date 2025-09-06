package com.github.singularity.core.server.auth

typealias Token = String

interface AuthTokenRepository {

    suspend fun generateAuthToken(nodeId: String, defaultGroupId: String): Token

    suspend fun getNodeId(token: Token, defaultGroupId: String): String?

}
