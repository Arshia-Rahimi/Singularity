package com.github.singularity.authentication

import com.github.singularity.models.Node
import io.ktor.server.auth.BearerTokenCredential

interface AuthRepository {

    fun getNode(token: BearerTokenCredential): Node?

    fun authenticate(node: Node): String

}
