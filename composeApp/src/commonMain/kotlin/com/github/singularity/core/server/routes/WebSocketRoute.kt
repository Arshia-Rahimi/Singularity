package com.github.singularity.core.server.routes

import com.github.singularity.core.data.BroadcastRepository
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.websocket.webSocket

fun Route.webSocketRoute(
    broadcastRepo: BroadcastRepository,
) {
    authenticate("auth") {
        webSocket("/sync") {
            // todo
        }
    }
}
