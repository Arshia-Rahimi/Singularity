package com.github.singularity.core.server

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.PairRequestRepository
import com.github.singularity.core.server.auth.AuthTokenRepository
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.model.HostedSyncGroupNode
import com.github.singularity.core.shared.model.http.PairCheckRequest
import com.github.singularity.core.shared.model.http.PairCheckResponse
import com.github.singularity.core.shared.model.http.PairRequest
import com.github.singularity.core.shared.model.http.PairResponse
import com.github.singularity.core.shared.model.http.PairStatus
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlin.random.Random

class KtorHttpServer(
    private val hostedSyncGroupRepo: HostedSyncGroupRepository,
    private val authTokenRepo: AuthTokenRepository,
    private val pairRequestRepo: PairRequestRepository,
    scope: CoroutineScope,
) {

    private val defaultSyncGroup = hostedSyncGroupRepo.defaultGroup
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), null)

    private val _isServerRunning = MutableStateFlow(false)
    val isServerRunning = MutableStateFlow(false)

    private val server = scope.embeddedServer(
        factory = CIO,
        port = SERVER_PORT,
        host = "0.0.0.0",
        module = { registerRoutes() },
    ).apply {
        monitor.subscribe(ApplicationStarted) {
            _isServerRunning.value = true
        }
        monitor.subscribe(ApplicationStopped) {
            _isServerRunning.value = false
        }
    }

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

                if (group == null || pairRequest.deviceId != group.hostedSyncGroupId) {
                    call.respond(PairResponse(false))
                    return@post
                }

                val requestId = Random.nextLong()
                pairRequestRepo.add(requestId, pairRequest)

                call.respond(PairResponse(true, requestId))

            }

            get("/pairCheck") {
                val request = call.receive<PairCheckRequest>()
                val group = defaultSyncGroup.value
                val pairCheck = pairRequestRepo.get(request.pairRequestId)

                if (group == null || group.hostedSyncGroupId != request.groupId || pairCheck == null) {
                    call.respond(PairCheckResponse(PairStatus.Error))
                    return@get
                }


                if (pairCheck.status != PairStatus.Approved) {
                    call.respond(PairCheckResponse(pairCheck.status))
                    return@get
                }

                val authToken =
                    authTokenRepo.generateAuthToken(request.groupId, group.hostedSyncGroupId)

                val node = HostedSyncGroupNode(
                    nodeId = pairCheck.node.deviceId,
                    nodeOs = pairCheck.node.deviceOs,
                    nodeName = pairCheck.node.deviceName,
                    authToken = authToken,
                    syncGroupId = group.hostedSyncGroupId,
                    syncGroupName = group.name,
                )

                call.respond(
                    PairCheckResponse(
                        pairStatus = pairCheck.status,
                        node = node,
                    )
                )

                pairRequestRepo.remove(request.pairRequestId)
                hostedSyncGroupRepo.create(node)
            }
        }
    }

}
