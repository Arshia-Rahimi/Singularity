package com.github.singularity.core.server.routes

import com.github.singularity.core.data.SyncEventRepository
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.websocket.webSocket

fun Route.webSocketRoute(
    syncEventRepo: SyncEventRepository,
) {
    authenticate("auth") {
        webSocket("/sync") {
            // todo
        }
    }
}
