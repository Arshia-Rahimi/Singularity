package com.github.singularity.core.server

import com.github.singularity.authentication.AuthRepository
import com.github.singularity.models.Node
import io.ktor.server.auth.BearerTokenCredential

class LocalServerAuthRepository : AuthRepository {

    override fun getNode(token: BearerTokenCredential): Node? {
        TODO("Not yet implemented")
    }

    override fun authenticate(node: Node): String {
        TODO("Not yet implemented")
    }

}
