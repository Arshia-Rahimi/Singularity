package com.github.singularity.authentication

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.bearer

fun Application.registerAuthentication() {
    install(Authentication) {
        bearer {
            authenticate {
                // todo
            }
        }
    }
}
