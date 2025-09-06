package com.github.singularity.core.server

import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.SyncEventRepository
import com.github.singularity.core.server.auth.AuthTokenRepository
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.model.Node
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.bearer
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

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
            install(WebSockets)
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
            authenticate("auth") {
                webSocket("/sync") {
                    // todo
                }
            }
        }
    }

}
