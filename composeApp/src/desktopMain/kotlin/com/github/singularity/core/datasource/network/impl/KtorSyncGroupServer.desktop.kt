package com.github.singularity.core.datasource.network.impl

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.datasource.database.HostedSyncGroupModel
import com.github.singularity.core.datasource.database.HostedSyncGroupNodeModel
import com.github.singularity.core.datasource.network.AuthTokenDataSource
import com.github.singularity.core.datasource.network.PairCheckRequestDto
import com.github.singularity.core.datasource.network.PairCheckResponseDto
import com.github.singularity.core.datasource.network.PairRequestDataSource
import com.github.singularity.core.datasource.network.PairRequestDto
import com.github.singularity.core.datasource.network.PairResponseDto
import com.github.singularity.core.datasource.network.PairStatus
import com.github.singularity.core.datasource.network.SyncGroupServer
import com.github.singularity.core.log.Logger
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.syncservice.SyncEventBridge
import com.github.singularity.core.syncservice.plugin.SyncEvent
import com.github.singularity.core.syncservice.plugin.syncEventJson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.createApplicationPlugin
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
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.random.Random

class KtorSyncGroupServer(
    private val syncEventBridge: SyncEventBridge,
    private val authTokenRepo: AuthTokenDataSource,
    private val pairRequestRepo: PairRequestDataSource,
    private val hostedSyncGroupRepo: HostedSyncGroupRepository,
    private val logger: Logger,
) : SyncGroupServer {

    private var syncGroupId: String? = null

    private val _connectedNodes = MutableStateFlow<List<HostedSyncGroupNodeModel>>(emptyList())
    override val connectedNodes = _connectedNodes.asStateFlow()

    private var serverStatusPlugin = createApplicationPlugin("ServerStatusPlugin") {
        onCallReceive {
            if (syncGroupId == null)
                it.respond(HttpStatusCode.NotImplemented)
        }
    }

    init {
        embeddedServer(
            factory = CIO,
            port = SERVER_PORT,
            host = "0.0.0.0",
        ) {
            install(serverStatusPlugin)

            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(syncEventJson)
                maxFrameSize = Long.MAX_VALUE
            }

            install(ContentNegotiation) {
                json(syncEventJson)
            }

            install(Authentication.Companion) {
                bearer("auth") {
                    authenticate {
                        authTokenRepo.getNode(it.token)
                    }
                }
            }


            registerRoutes()
        }.start(wait = false)
    }

    override fun start(group: HostedSyncGroupModel) {
        _connectedNodes.value = emptyList()
        syncGroupId = group.hostedSyncGroupId
    }

    override fun stop() {
        _connectedNodes.value = emptyList()
        syncGroupId = null
    }

    private fun Application.registerRoutes() {
        routing {
            // websocket
            authenticate("auth", strategy = AuthenticationStrategy.Required) {
                webSocket("/ws/sync") {
                    val converter = converter ?: return@webSocket

                    val node = call.principal<HostedSyncGroupNodeModel>()
                    if (node == null || node.syncGroupId != syncGroupId) {
                        close(
                            CloseReason(
                                CloseReason.Codes.VIOLATED_POLICY,
                                "authentication failed"
                            )
                        )
                        return@webSocket
                    }

                    _connectedNodes.value += node

                    val sendJob = launch {
                        syncEventBridge.outgoingSyncEvents.collect { event ->
                            try {
                                sendSerialized(event)
                            } catch (e: Exception) {
                                logger.e("outgoingEvent error", e)
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
                            logger.e("incomingEvent error", e)
                        }
                    }

                    joinAll(receiveJob, sendJob)

                }
            }

            // http
            contentType(ContentType.Application.Json) {
                post("/api/pair") {
                    val groupId = syncGroupId ?: return@post
                    val pairRequest = call.receive<PairRequestDto>()

                    if (pairRequest.syncGroupId != groupId) {
                        call.respond(PairResponseDto(false))
                        return@post
                    }

                    val hasRequestedBefore = pairRequestRepo.get(pairRequest.deviceId) != null
                    if (hasRequestedBefore) {
                        call.respond(PairResponseDto(false))
                    }

                    val hasPairedBefore = hostedSyncGroupRepo.hasPairedBefore(
                        pairRequest.deviceId,
                        pairRequest.syncGroupId
                    )
                    if (hasPairedBefore) {
                        hostedSyncGroupRepo.delete(pairRequest.syncGroupId, pairRequest.deviceId)
                    }

                    val requestId = Random.nextInt(1000000000, Int.MAX_VALUE)
                    pairRequestRepo.add(requestId, pairRequest)

                    call.respond(PairResponseDto(true, requestId))

                }

                get("/api/pairCheck") {
                    val groupId = syncGroupId ?: return@get
                    val request = call.receive<PairCheckRequestDto>()
                    val pairCheck = pairRequestRepo.get(request.pairRequestId)

                    if (groupId != request.syncGroupId || pairCheck == null) {
                        call.respond(PairCheckResponseDto(PairStatus.Error))
                        return@get
                    }

                    if (pairCheck.status != PairStatus.Approved) {
                        call.respond(PairCheckResponseDto(pairCheck.status))

                        if (pairCheck.status == PairStatus.Rejected)
                            pairRequestRepo.remove(pairCheck.requestId)

                        return@get
                    }

                    val hostedNode = authTokenRepo.generateAuthToken(pairCheck.node)

                    if (hostedNode == null) {
                        call.respond(PairCheckResponseDto(PairStatus.Error))
                        return@get
                    }

                    pairRequestRepo.remove(request.pairRequestId)

                    call.respond(
                        PairCheckResponseDto(
                            pairStatus = pairCheck.status,
                            node = hostedNode,
                        )
                    )

                }
            }
        }
    }

}
