package com.github.singularity.authentication

import io.ktor.server.auth.BearerTokenCredential

interface AuthRepository {

    fun getDevice(token: BearerTokenCredential): String?

}
