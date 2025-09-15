package com.github.singularity.core.server

import com.github.singularity.core.shared.model.Node
import io.ktor.server.auth.principal
import io.ktor.server.websocket.DefaultWebSocketServerSession

fun DefaultWebSocketServerSession.node() = call.principal<Node>()!!
