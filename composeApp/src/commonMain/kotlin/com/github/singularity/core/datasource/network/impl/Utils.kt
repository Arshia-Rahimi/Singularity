package com.github.singularity.core.datasource.network.impl

import com.github.singularity.core.shared.model.HostedSyncGroupNode
import com.github.singularity.core.shared.model.http.PairCheckRequest
import com.github.singularity.core.shared.model.http.PairRequest
import io.ktor.server.auth.principal
import io.ktor.server.routing.RoutingContext
import io.ktor.server.websocket.DefaultWebSocketServerSession

fun DefaultWebSocketServerSession.getNode() = call.principal<HostedSyncGroupNode>()!!

fun RoutingContext.getPairRequest() = call.principal<PairRequest>()!!

fun RoutingContext.getPairCheckRequest() = call.principal<PairCheckRequest>()!!
