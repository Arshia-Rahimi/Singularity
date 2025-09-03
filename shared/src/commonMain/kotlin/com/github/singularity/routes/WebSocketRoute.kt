package com.github.singularity.routes

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.routing
import io.ktor.server.websocket.webSocket

fun Application.registerWebSocketRoute() {
    routing {
        authenticate {
            webSocket("/sync") {
                // todo
            }
        }
    }
}
