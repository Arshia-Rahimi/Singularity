package com.github.singularity.authentication

import io.ktor.server.auth.BearerTokenCredential

class ServerAuthRepository : AuthRepository {

    override fun getDevice(token: BearerTokenCredential): String? {
        TODO("Not yet implemented")
    }

}