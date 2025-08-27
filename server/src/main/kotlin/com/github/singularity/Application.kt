package com.github.singularity

import com.github.singularity.di.registerDi
import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer

fun main() {
    embeddedServer(
        factory = CIO,
        port = SERVER_PORT,
        host = "0.0.0.0",
        module = Application::module,
    ).start(wait = true)
}

fun Application.module() {
    Modules.forEach { module -> module() }
}

val Modules = SharedModules + listOf(
    Application::registerDi,
)
