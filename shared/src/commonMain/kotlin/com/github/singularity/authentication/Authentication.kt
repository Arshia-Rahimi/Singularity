package com.github.singularity.authentication

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.bearer
import org.koin.mp.KoinPlatform.getKoin

fun Application.registerAuthentication() {
    val authRepo by getKoin().inject<AuthRepository>()

    install(Authentication) {
        bearer {
            authenticate { token ->
                authRepo.getNode(token)
            }
        }
    }
}
