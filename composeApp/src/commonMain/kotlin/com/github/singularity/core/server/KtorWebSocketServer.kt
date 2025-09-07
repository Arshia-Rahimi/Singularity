package com.github.singularity.core.server

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.SyncEventRepository
import com.github.singularity.core.server.auth.AuthTokenRepository
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.websocket.SyncEvent
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.serialize
import io.ktor.server.application.Application
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json

class KtorWebSocketServer(
    private val syncEventRepo: SyncEventRepository,
    private val authTokenRepo: AuthTokenRepository,
    hostedSyncGroupRepo: HostedSyncGroupRepository,
    scope: CoroutineScope,
) {

    private val defaultSyncGroup = hostedSyncGroupRepo.defaultGroup
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), null)

    private val _connectedNodes = mutableListOf<Node>()
    val connctedNodes = _connectedNodes.toList()

    private val server = scope.embeddedServer(
        factory = CIO,
        port = SERVER_PORT,
        host = "0.0.0.0",
        module = {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(
                    Json { ignoreUnknownKeys = true }
                )
            }
            install(Authentication) {
                bearer {
                    authenticate { token ->
                        val defaultGroup = defaultSyncGroup.value ?: return@authenticate null
                        defaultGroup.nodes.firstOrNull {
                            it.nodeId == authTokenRepo.getNodeId(
                                token.token,
                                defaultGroup.hostedSyncGroupId
                            )
                        }
                    }
                }
            }
            registerRoutes()
        },
    )

    fun start() {
        server.start()
    }

    fun stop() {
        server.stop()
    }

    private fun Application.registerRoutes() {
        routing {
            authenticate("auth", strategy = AuthenticationStrategy.Required) {
                webSocket("/sync") {
                    val converter = converter ?: return@webSocket
                    _connectedNodes += call.principal<Node>()!!

                    try {

                        incoming.receiveAsFlow()
                            .filterIsInstance<Frame.Text>()
                            .map { converter.deserialize<SyncEvent>(it) }
                            .collect { syncEventRepo.incomingEventCallback(it) }

                        syncEventRepo.outgoingSyncEvents
                            .map { converter.serialize<SyncEvent>(it) }
                            .collect { send(it) }

                    } finally {
                        _connectedNodes -= call.principal<Node>()!!
                    }
                }
            }
        }
    }

}
