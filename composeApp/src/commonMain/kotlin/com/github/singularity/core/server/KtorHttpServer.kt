package com.github.singularity.core.server

import com.github.singularity.core.data.AuthTokenRepository
import com.github.singularity.core.shared.HTTP_SERVER_PORT
import com.github.singularity.core.shared.model.HostedSyncGroup
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
	private val authTokenRepo: AuthTokenRepository,
	private val pairRequestRepo: PairRequestDataSource,
) {

    private var syncGroupId: String? = null

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

    fun start(group: HostedSyncGroup) {
        syncGroupId = group.hostedSyncGroupId
        server.start()
    }

    fun stop() {
        server.stop()
        syncGroupId = null
    }

    private fun Application.registerRoutes() {
        routing {
            contentType(ContentType.Application.Json) {
                post("/pair") {
                    val groupId = syncGroupId
                    val pairRequest = call.receive<PairRequest>()

                    if (groupId == null || pairRequest.syncGroupId != groupId) {
                        call.respond(PairResponse(false))
                        return@post
                    }

                    val requestId = Random.nextInt(1000000000, Int.MAX_VALUE)
                    pairRequestRepo.add(requestId, pairRequest)

                    call.respond(PairResponse(true, requestId))

                }

                get("/pairCheck") {
                    val groupId = syncGroupId
                    val request = call.receive<PairCheckRequest>()
                    val pairCheck = pairRequestRepo.get(request.pairRequestId)

                    if (groupId == null || groupId != request.syncGroupId || pairCheck == null) {
                        call.respond(PairCheckResponse(PairStatus.Error))
                        return@get
                    }


                    if (pairCheck.status != PairStatus.Approved) {
                        call.respond(PairCheckResponse(pairCheck.status))
                        return@get
                    }

                    val hostedNode = authTokenRepo.generateAuthToken(pairCheck.node)

                    if (hostedNode == null) {
                        call.respond(PairCheckResponse(PairStatus.Error))
                        return@get
                    }

                    pairRequestRepo.remove(request.pairRequestId)

                    call.respond(
                        PairCheckResponse(
                            pairStatus = pairCheck.status,
                            node = hostedNode,
                        )
                    )

                }
            }
        }
    }

}
