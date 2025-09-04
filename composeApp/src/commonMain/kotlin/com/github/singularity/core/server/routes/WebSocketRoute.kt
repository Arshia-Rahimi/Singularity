package com.github.singularity.core.server.routes

import com.github.singularity.core.data.BroadcastRepository
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.routing
import io.ktor.server.websocket.webSocket

fun Application.registerWebSocketRoute(
    broadcastRepo: BroadcastRepository,
) {
    routing {
        authenticate {
            webSocket("/sync") {
                // todo
            }
        }
    }
}
