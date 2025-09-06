package com.github.singularity.core.server

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.server.auth.AuthTokenRepository
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.model.HostedSyncGroupNode
import com.github.singularity.core.shared.model.http.PairRequest
import com.github.singularity.core.shared.model.http.PairResponse
import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class KtorHttpServer(
    private val hostedSyncGroupRepo: HostedSyncGroupRepository,
    private val authTokenRepo: AuthTokenRepository,
    scope: CoroutineScope,
) {

    private val defaultSyncGroup = hostedSyncGroupRepo.defaultGroup
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), null)

    private val server = scope.embeddedServer(
        factory = CIO,
        port = SERVER_PORT,
        host = "0.0.0.0",
        module = { registerRoutes() },
    )

    fun start() {
        server.start()
    }

    fun stop() {
        server.stop()
    }

    private fun Application.registerRoutes() {
        routing {
            post("/pair") {
                val group = defaultSyncGroup.value
                val pairRequest = call.receive<PairRequest>()

                if (group == null || pairRequest.nodeId != group.hostedSyncGroupId) {
                    call.respond(PairResponse(false, "server not running"))
                    return@post
                }

                val isApproved = false // todo

                if (!isApproved) {
                    call.respond(PairResponse(false, "denied to join"))
                    return@post
                }

                val authToken =
                    authTokenRepo.generateAuthToken(pairRequest.nodeId, group.hostedSyncGroupId)

                val node = HostedSyncGroupNode(
                    nodeId = pairRequest.nodeId,
                    nodeOs = pairRequest.nodeOs,
                    nodeName = pairRequest.nodeName,
                    authToken = authToken,
                    syncGroupId = group.hostedSyncGroupId,
                    syncGroupName = group.name,
                )

                hostedSyncGroupRepo.create(node)
                call.respond(
                    PairResponse(
                        success = true,
                        node = node,
                    )
                )
            }
        }
    }

}
