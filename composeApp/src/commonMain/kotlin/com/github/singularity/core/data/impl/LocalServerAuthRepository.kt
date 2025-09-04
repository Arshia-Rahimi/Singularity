package com.github.singularity.core.data.impl

import com.github.singularity.core.data.AuthRepository
import com.github.singularity.core.shared.model.Node
import io.ktor.server.auth.BearerTokenCredential

class LocalServerAuthRepository : AuthRepository {

    override fun getNode(token: BearerTokenCredential): Node? {
        TODO("Not yet implemented")
    }

    override fun authenticate(node: Node): String {
        TODO("Not yet implemented")
    }

}
