package com.github.singularity.core.datasource.network.impl

import com.github.singularity.core.datasource.memory.AuthTokenDataSource
import com.github.singularity.core.datasource.memory.PairRequestDataSource
import com.github.singularity.core.datasource.memory.SyncEventBridge
import com.github.singularity.core.datasource.network.SyncGroupServer
import com.github.singularity.core.log.Logger
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.HostedSyncGroupNode
import com.github.singularity.core.shared.model.http.PairCheckRequest
import com.github.singularity.core.shared.model.http.PairCheckResponse
import com.github.singularity.core.shared.model.http.PairRequest
import com.github.singularity.core.shared.model.http.PairResponse
import com.github.singularity.core.shared.model.http.PairStatus
import com.github.singularity.core.syncservice.plugin.SyncEvent
import io.ktor.http.ContentType
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.AuthenticationStrategy
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.bearer
import io.ktor.server.auth.principal
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.contentType
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.converter
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.random.Random

class KtorSyncGroupServer(
    private val syncEventBridge: SyncEventBridge,
    private val authTokenRepo: AuthTokenDataSource,
    private val pairRequestRepo: PairRequestDataSource,
    private val logger: Logger,
) : SyncGroupServer {

    private var syncGroupId: String? = null

    private val _connectedNodes = MutableStateFlow<List<HostedSyncGroupNode>>(emptyList())
    override val connectedNodes = _connectedNodes.asStateFlow()

    private var isRunning = false

    private val server = embeddedServer(
        factory = CIO,
        port = SERVER_PORT,
        host = "0.0.0.0",
    ) {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
            maxFrameSize = Long.MAX_VALUE
        }

        install(ContentNegotiation) {
            json()
        }

        install(Authentication.Companion) {
            bearer("auth") {
                authenticate {
                    authTokenRepo.getNode(it.token)
                }
            }
        }

        registerRoutes()
    }.apply {
        monitor.subscribe(ApplicationStarted) {
            isRunning = true
        }
        monitor.subscribe(ApplicationStopped) {
            isRunning = false
        }
    }

    override fun start(group: HostedSyncGroup) {
        if (isRunning) {
            server.stop()
        }
        _connectedNodes.value = emptyList()
        syncGroupId = group.hostedSyncGroupId
        server.start()
    }

    override fun stop() {
        server.stop()
        _connectedNodes.value = emptyList()
        syncGroupId = null
    }

    private fun Application.registerRoutes() {
        routing {
            // websocket
            authenticate("auth", strategy = AuthenticationStrategy.Required) {
                webSocket("/ws/sync") {
                    val converter = converter ?: return@webSocket

                    val node = call.principal<HostedSyncGroupNode>() ?: return@webSocket

                    _connectedNodes.value += node

                    val sendJob = launch {
                        syncEventBridge.outgoingSyncEvents.collect { event ->
                            try {
                                sendSerialized(event)
                            } catch (e: Exception) {
                                logger.e(this::class.simpleName, "outgoingEvent error", e)
                            }
                        }
                    }

                    val receiveJob = launch {
                        try {
                            incoming.consumeEach { frame ->
                                if (frame !is Frame.Text) return@consumeEach
                                val event = converter.deserialize<SyncEvent>(frame)
                                syncEventBridge.incomingEventCallback(event)
                            }
                        } catch (e: Exception) {
                            logger.e(this::class.simpleName, "incomingEvent error", e)
                        }
                    }

                    joinAll(receiveJob, sendJob)

                }
            }

            // http
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
