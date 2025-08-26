package com.github.singularity.websocket

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.websocket.WebSockets

fun Application.registerWebsocket() {
    install(WebSockets) {

    }
}
