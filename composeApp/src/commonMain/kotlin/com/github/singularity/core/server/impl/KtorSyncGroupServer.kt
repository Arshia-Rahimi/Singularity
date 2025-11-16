package com.github.singularity.core.server.impl

import com.github.singularity.core.data.AuthTokenRepository
import com.github.singularity.core.server.PairRequestDataSource
import com.github.singularity.core.server.SyncGroupServer
import com.github.singularity.core.server.getNode
import com.github.singularity.core.server.getPairCheckRequest
import com.github.singularity.core.server.getPairRequest
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.HostedSyncGroupNode
import com.github.singularity.core.shared.model.http.PairCheckResponse
import com.github.singularity.core.shared.model.http.PairResponse
import com.github.singularity.core.shared.model.http.PairStatus
import com.github.singularity.core.sync.SyncEvent
import com.github.singularity.core.sync.SyncEventBridge
import io.ktor.http.ContentType
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.serialization.serialize
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.AuthenticationStrategy
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.bearer
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.contentType
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.converter
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.random.Random

class KtorSyncGroupServer(
    private val syncEventBridge: SyncEventBridge,
    private val authTokenRepo: AuthTokenRepository,
    private val pairRequestRepo: PairRequestDataSource,
) : SyncGroupServer {

    private var syncGroupId: String? = null

    private val _connectedNodes = MutableStateFlow<List<HostedSyncGroupNode>>(emptyList())
    override val connectedNodes = _connectedNodes.asStateFlow()

    private var isRunning = false

    private val server = embeddedServer(
        factory = CIO,
        port = SERVER_PORT,
        host = "0.0.0.0"
    ) {
        install(WebSockets.Plugin) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
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
                webSocket("/sync") {
                    try {
                        val converter = converter ?: return@webSocket

                        _connectedNodes.value += getNode()

                        launch {
                            try {
                                incoming.consumeAsFlow()
                                    .filterIsInstance<Frame.Text>()
                                    .map { converter.deserialize<SyncEvent>(it) }
                                    .collect { syncEventBridge.incomingEventCallback(it) }
                            } catch (e: Exception) {
                                println(e.message)
                                e.printStackTrace()
                            }
                        }

                        launch {
                            try {
                                syncEventBridge.outgoingSyncEvents
                                    .map { converter.serialize<SyncEvent>(it) }
                                    .collect {
                                        try {
                                            send(it)
                                        } catch (e: Exception) {
                                            println(e.message)
                                            e.printStackTrace()
                                        }
                                    }
                            } catch (e: Exception) {
                                println(e.message)
                                e.printStackTrace()
                            }
                        }

                    } finally {
                        _connectedNodes.value -= getNode()
                    }
                }
            }

            // http
            contentType(ContentType.Application.Json) {
                post("/pair") {
                    val groupId = syncGroupId
                    val pairRequest = getPairRequest()

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
                    val request = getPairCheckRequest()
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
