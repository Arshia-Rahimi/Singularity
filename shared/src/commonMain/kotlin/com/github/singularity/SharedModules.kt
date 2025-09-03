package com.github.singularity

import com.github.singularity.authentication.AuthRepository
import com.github.singularity.routes.registerPairingRoute
import com.github.singularity.routes.registerWebSocketRoute
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.bearer
import io.ktor.server.websocket.WebSockets
import org.koin.mp.KoinPlatform.getKoin

fun Application.registerWebsocket() {
    install(WebSockets)
}

fun Application.registerRoutes() {
    registerPairingRoute()
    registerWebSocketRoute()
}

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

val SharedModules = listOf<Application.() -> Unit>(
    Application::registerRoutes,
    Application::registerAuthentication,
    Application::registerWebsocket,
)
