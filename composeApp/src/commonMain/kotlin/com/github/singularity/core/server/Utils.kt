package com.github.singularity.core.server

import com.github.singularity.core.shared.model.HostedSyncGroupNode
import io.ktor.server.auth.principal
import io.ktor.server.websocket.DefaultWebSocketServerSession

fun DefaultWebSocketServerSession.node() = call.principal<HostedSyncGroupNode>()!!
