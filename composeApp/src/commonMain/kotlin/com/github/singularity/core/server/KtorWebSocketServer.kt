package com.github.singularity.core.server

import com.github.singularity.core.data.SyncEventRepository
import com.github.singularity.core.server.auth.AuthTokenRepository
import com.github.singularity.core.shared.WEBSOCKET_SERVER_PORT
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.websocket.SyncEvent
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.serialize
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
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.converter
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.json.Json

class KtorWebSocketServer(
    private val syncEventRepo: SyncEventRepository,
    private val authTokenRepo: AuthTokenRepository,
) {

    private var syncGroup: HostedSyncGroup? = null

    private val _connectedNodes = MutableStateFlow<List<Node>>(emptyList())
    val connectedNodes = _connectedNodes.asStateFlow()

    private val _isServerRunning = MutableStateFlow(false)
    val isServerRunning = _isServerRunning.asStateFlow()

    private val server = embeddedServer(
        factory = CIO,
        port = WEBSOCKET_SERVER_PORT,
        host = "0.0.0.0"
    ) {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(
                Json { ignoreUnknownKeys = true }
            )
        }
        install(Authentication) {
            bearer {
                authenticate { token ->
                    val group = syncGroup ?: return@authenticate null
                    group.nodes.firstOrNull {
                        it.nodeId == authTokenRepo.getNodeId(
                            token.token,
                            group.hostedSyncGroupId,
                        )
                    }
                }
            }
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
            authenticate("auth", strategy = AuthenticationStrategy.Required) {
                webSocket("/sync") {
                    val converter = converter ?: return@webSocket
                    _connectedNodes.value = _connectedNodes.value + call.principal<Node>()!!

                    try {

                        incoming.receiveAsFlow()
                            .filterIsInstance<Frame.Text>()
                            .map { converter.deserialize<SyncEvent>(it) }
                            .collect { syncEventRepo.incomingEventCallback(it) }

                        syncEventRepo.outgoingSyncEvents
                            .map { converter.serialize<SyncEvent>(it) }
                            .collect { send(it) }

                    } finally {
                        _connectedNodes.value = _connectedNodes.value - call.principal<Node>()!!
                    }
                }
            }
        }
    }

}
