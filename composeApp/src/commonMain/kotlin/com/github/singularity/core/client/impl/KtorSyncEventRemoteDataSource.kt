package com.github.singularity.core.client.impl

import com.github.singularity.core.client.SyncEventRemoteDataSource
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.sync.SyncEvent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.http.HttpHeaders
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.serialize
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class KtorSyncEventRemoteDataSource : SyncEventRemoteDataSource {

    private var authToken: String? = null

    val authPlugin = createClientPlugin("AuthPlugin") {
        onRequest { request, _ ->
            authToken?.let {
                request.headers[HttpHeaders.Authorization] = "Bearer $it"
            }
        }
    }

    var wsSession: DefaultClientWebSocketSession? = null
        private set

    private val client = HttpClient(CIO) {
        install(authPlugin)

        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }

    override suspend fun connect(server: LocalServer, token: String) {
        try {
            authToken = token
            wsSession = client.webSocketSession(
                host = server.ip,
	            port = SERVER_PORT,
                path = "/sync",
            )
        } finally {
            authToken = null
            wsSession = null
        }
    }

    override suspend fun disconnect() {
        val session = wsSession ?: return
        session.close(CloseReason(CloseReason.Codes.NORMAL, "Client disconnect"))
    }

    override fun incomingEventsFlow() = flow {
        val session = wsSession ?: return@flow
        val converter = session.converter ?: return@flow

        session.incoming.consumeAsFlow()
            .filterIsInstance<Frame.Text>()
            .map { converter.deserialize<SyncEvent>(it) }
            .let { emitAll(it) }
    }

    override suspend fun send(event: SyncEvent) {
        val session = wsSession ?: return
        val converter = session.converter ?: return
        session.send(converter.serialize(event))
    }

}
