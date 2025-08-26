package com.github.singularity

import com.github.singularity.authentication.registerAuthentication
import com.github.singularity.routing.registerPairingRoute
import io.ktor.server.application.Application

val SharedModules = listOf<Application.() -> Unit>(
    Application::registerPairingRoute,
    Application::registerAuthentication,
)
