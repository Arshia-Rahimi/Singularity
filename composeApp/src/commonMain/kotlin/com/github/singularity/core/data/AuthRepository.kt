package com.github.singularity.core.data

import com.github.singularity.core.shared.model.Node
import io.ktor.server.auth.BearerTokenCredential

interface AuthRepository {

    fun getNode(token: BearerTokenCredential): Node?

    fun authenticate(node: Node): String

}
