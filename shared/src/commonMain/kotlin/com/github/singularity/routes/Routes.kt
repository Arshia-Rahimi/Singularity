package com.github.singularity.routes

import io.ktor.server.application.Application

fun Application.registerRoutes() {
    registerPairingRoute()
    registerWebSocketRoute()
}
