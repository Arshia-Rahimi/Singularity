package com.github.singularity.core.server

import com.github.singularity.core.data.AuthTokenRepository
import com.github.singularity.core.data.SyncEventBridge
import com.github.singularity.core.shared.WEBSOCKET_SERVER_PORT
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.HostedSyncGroupNode
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
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.converter
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class KtorWebSocketServer(
    private val syncEventBridge: SyncEventBridge,
    private val authTokenRepo: AuthTokenRepository,
) {

    private var syncGroup: HostedSyncGroup? = null

    private val _connectedNodes = MutableStateFlow<List<HostedSyncGroupNode>>(emptyList())
    val connectedNodes = _connectedNodes.asStateFlow()

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
            bearer("auth") {
                authenticate {
                    authTokenRepo.getNode(it.token)
                }
            }
        }
        registerRoutes()
    }

    fun start(group: HostedSyncGroup) {
        syncGroup = group
        server.start()
    }

    fun stop() {
        server.stop()
        _connectedNodes.value = emptyList()
        syncGroup = null
    }

    private fun Application.registerRoutes() {
        routing {
            authenticate("auth", strategy = AuthenticationStrategy.Required) {
                webSocket("/sync") {
                    try {
                        val converter = converter ?: return@webSocket

                        _connectedNodes.value = _connectedNodes.value + node()

                        coroutineScope {
                            launch {
                                try {
                                    incoming.receiveAsFlow()
                                        .filterIsInstance<Frame.Text>()
                                        .map { converter.deserialize<SyncEvent>(it) }
                                        .onEach { println("received $it") }
                                        .collect { syncEventBridge.incomingEventCallback(it) }
                                } catch (e: Exception) {
                                    println(e.message)
                                    e.printStackTrace()
                                }
                            }

                            launch {
                                try {
                                    syncEventBridge.outgoingSyncEvents
                                        .onEach { println("sent $it") }
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
                        }

                    } finally {
                        _connectedNodes.value = _connectedNodes.value - node()
                    }
                }
            }
        }
    }

}
