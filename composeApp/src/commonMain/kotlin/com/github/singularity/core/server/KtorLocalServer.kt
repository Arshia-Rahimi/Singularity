package com.github.singularity.core.server

import com.github.singularity.core.data.AuthRepository
import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.server.routes.pairingRoute
import com.github.singularity.core.server.routes.webSocketRoute
import com.github.singularity.core.shared.SERVER_PORT
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.bearer
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets

class KtorLocalServer(
    private val authRepo: AuthRepository,
    private val broadcastRepo: BroadcastRepository,
) {

    private val server = embeddedServer(
        factory = CIO,
        port = SERVER_PORT,
        host = "0.0.0.0",
        module = {
            registerWebsockets()
            registerAuthentication()
            registerRoutes()
        },
    )

    fun start() {
        server.start()
    }

    fun stop() {
        server.stop()
    }

    private fun Application.registerWebsockets() {
        install(WebSockets)
    }

    private fun Application.registerAuthentication() {
        install(Authentication) {
            bearer {
                authenticate { authRepo.getNode(it.token) }
            }
        }
    }

    private fun Application.registerRoutes() {
        routing {
            pairingRoute(authRepo)
            webSocketRoute(broadcastRepo)
        }
    }

}
