package com.github.singularity.core.server

import com.github.singularity.core.data.AuthRepository
import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.shared.SERVER_PORT
import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer

class KtorLocalServer(
    private val authRepo: AuthRepository,
    private val broadcastRepo: BroadcastRepository,
) {

    private val serverModules: Application.() -> Unit = {
        registerWebsocket()
        registerRoutes(authRepo, broadcastRepo)
        registerAuthentication(authRepo)
    }

    private val server = embeddedServer(
        factory = CIO,
        port = SERVER_PORT,
        host = "0.0.0.0",
        module = serverModules,
    ).start(wait = true)

}
