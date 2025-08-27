package com.github.singularity.core.server

import com.github.singularity.authentication.AuthRepository
import io.ktor.server.auth.BearerTokenCredential

class LocalServerAuthRepository : AuthRepository {

    override fun getDevice(token: BearerTokenCredential): String? {
        TODO("Not yet implemented")
    }

}
