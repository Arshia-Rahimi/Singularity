package com.github.singularity.core.server

import com.github.singularity.SERVER_PORT
import com.github.singularity.SharedModules
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer

class KtorLocalServer {

    private val localModules = SharedModules + listOf()

    private val server = embeddedServer(
        factory = CIO,
        port = SERVER_PORT,
        host = "0.0.0.0",
        module = { localModules.forEach { module -> module() } },
    ).start(wait = true)

}
