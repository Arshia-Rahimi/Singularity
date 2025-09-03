package com.github.singularity.authentication

import com.github.singularity.models.Node
import io.ktor.server.auth.BearerTokenCredential

class ServerAuthRepository : AuthRepository {

    override fun getNode(token: BearerTokenCredential): Node? {
        TODO("Not yet implemented")
    }

    override fun authenticate(node: Node): String {
        TODO("Not yet implemented")
    }

}
