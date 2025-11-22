package com.github.singularity.core.datasource.network.impl

import com.github.singularity.core.shared.model.HostedSyncGroupNode
import io.ktor.server.auth.principal
import io.ktor.server.websocket.DefaultWebSocketServerSession

fun DefaultWebSocketServerSession.getNode() = call.principal<HostedSyncGroupNode>()!!
