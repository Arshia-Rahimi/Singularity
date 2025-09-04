package com.github.singularity.core.server

import com.github.singularity.core.data.AuthRepository
import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.server.routes.registerPairingRoute
import com.github.singularity.core.server.routes.registerWebSocketRoute
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.bearer
import io.ktor.server.websocket.WebSockets

fun Application.registerWebsocket() {
    install(WebSockets)
}

fun Application.registerRoutes(
    authRepo: AuthRepository,
    broadcastRepo: BroadcastRepository,
) {
    registerPairingRoute(authRepo)
    registerWebSocketRoute(broadcastRepo)
}

fun Application.registerAuthentication(authRepo: AuthRepository) {
    install(Authentication) {
        bearer {
            authenticate { token ->
                authRepo.getNode(token)
            }
        }
    }
}
