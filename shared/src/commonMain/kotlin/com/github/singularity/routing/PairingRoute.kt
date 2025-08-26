package com.github.singularity.routing

import io.ktor.server.application.Application
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.registerPairingRoute() {
    routing {
        post("pair") {
            // todo
        }
    }
}
