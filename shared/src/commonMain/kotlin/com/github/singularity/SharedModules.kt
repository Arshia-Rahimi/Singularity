package com.github.singularity

import com.github.singularity.authentication.registerAuthentication
import com.github.singularity.routes.registerRoutes
import com.github.singularity.websocket.registerWebsocket
import io.ktor.server.application.Application

val SharedModules = listOf<Application.() -> Unit>(
    Application::registerRoutes,
    Application::registerAuthentication,
    Application::registerWebsocket,
)
