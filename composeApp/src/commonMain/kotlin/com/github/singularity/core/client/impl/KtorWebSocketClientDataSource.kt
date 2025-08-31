package com.github.singularity.core.client.impl

import com.github.singularity.core.client.WebSocketClientDataSource
import com.github.singularity.models.Server
import com.github.singularity.models.websocket.WebsocketResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.websocket.Frame
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class KtorWebSocketClientDataSource : WebSocketClientDataSource {

    private val client = HttpClient(CIO) {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(
                Json { ignoreUnknownKeys = true }
            )
        }
    }

    override fun connect(server: Server, authKey: String) = callbackFlow {
        client.webSocket("wss://${server.ip}/sync") {
            val converter = converter ?: return@webSocket

            val job = launch {
                incoming.receiveAsFlow()
                    .filterIsInstance<Frame.Text>()
                    .map { converter.deserialize<WebsocketResponse>(it) }
                    .collect { trySend(it) }
            }

            awaitClose {
                job.cancel()
                this@webSocket.cancel()
            }
        }
    }

}
