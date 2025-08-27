package com.github.singularity.authentication

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.bearer
import org.koin.ktor.ext.inject

fun Application.registerAuthentication() {
    val authRepo by inject<AuthRepository>()
    install(Authentication) {
        bearer {
            authenticate {
                authRepo.getDevice(it)
            }
        }
    }
}
