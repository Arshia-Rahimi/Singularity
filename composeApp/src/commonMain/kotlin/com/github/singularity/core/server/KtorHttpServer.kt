package com.github.singularity.core.server

import com.github.singularity.core.data.AuthTokenRepository
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.PairRequestRepository
import com.github.singularity.core.shared.HTTP_SERVER_PORT
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.HostedSyncGroupNode
import com.github.singularity.core.shared.model.http.PairCheckRequest
import com.github.singularity.core.shared.model.http.PairCheckResponse
import com.github.singularity.core.shared.model.http.PairRequest
import com.github.singularity.core.shared.model.http.PairResponse
import com.github.singularity.core.shared.model.http.PairStatus
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.contentType
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class KtorHttpServer(
    private val hostedSyncGroupRepo: HostedSyncGroupRepository,
    private val authTokenRepo: AuthTokenRepository,
    private val pairRequestRepo: PairRequestRepository,
) {

    private var syncGroup: HostedSyncGroup? = null

    private val _isServerRunning = MutableStateFlow(false)
    val isServerRunning = _isServerRunning.asStateFlow()

    private val server = embeddedServer(
        factory = CIO,
        port = HTTP_SERVER_PORT,
        host = "0.0.0.0",
    ) {
        install(ContentNegotiation) {
            json()
        }
        registerRoutes()
    }.apply {
        monitor.subscribe(ApplicationStarted) {
            _isServerRunning.value = true
        }
        monitor.subscribe(ApplicationStopped) {
            _isServerRunning.value = false
        }
    }

    suspend fun start(group: HostedSyncGroup) {
        syncGroup = group
        server.startSuspend()
    }

    suspend fun stop() {
        server.stopSuspend()
        syncGroup = null
    }

    private fun Application.registerRoutes() {
        routing {
            contentType(ContentType.Application.Json) {
                post("/pair") {
                    val group = syncGroup
                    val pairRequest = call.receive<PairRequest>()

                    if (group == null || pairRequest.syncGroupId != group.hostedSyncGroupId) {
                        call.respond(PairResponse(false))
                        return@post
                    }

                    val requestId = Random.nextLong(1000000000000000000L, Long.MAX_VALUE)
                    pairRequestRepo.add(requestId, pairRequest)

                    call.respond(PairResponse(true, requestId))

                }

                get("/pairCheck") {
                    val group = syncGroup
                    val request = call.receive<PairCheckRequest>()
                    val pairCheck = pairRequestRepo.get(request.pairRequestId)

                    if (group == null || group.hostedSyncGroupId != request.syncGroupId || pairCheck == null) {
                        call.respond(PairCheckResponse(PairStatus.Error))
                        return@get
                    }


                    if (pairCheck.status != PairStatus.Approved) {
                        call.respond(PairCheckResponse(pairCheck.status))
                        return@get
                    }

                    val authToken =
                        authTokenRepo.generateAuthToken(
                            request.syncGroupId,
                            group.hostedSyncGroupId
                        )

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

}
