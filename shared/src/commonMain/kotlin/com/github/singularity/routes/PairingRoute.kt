package com.github.singularity.routes

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
