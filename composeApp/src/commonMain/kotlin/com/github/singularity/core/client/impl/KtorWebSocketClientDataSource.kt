package com.github.singularity.core.client.impl

import com.github.singularity.core.client.WebSocketClientDataSource
import com.github.singularity.core.client.utils.WebSocketConnectionDroppedException
import com.github.singularity.core.client.utils.WebSocketConnectionFailedException
import com.github.singularity.models.IServer
import com.github.singularity.models.sync.SyncEvent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSocketException
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.websocket.Frame
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

class KtorWebSocketClientDataSource : WebSocketClientDataSource {

    private val client = HttpClient(CIO) {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(
                Json { ignoreUnknownKeys = true }
            )
        }
    }

    override fun connect(server: IServer, authToken: String) = callbackFlow {
        try {
            client.webSocket("wss://${server.ip}/sync") {
                val converter = converter ?: return@webSocket

                val job = launch {
                    incoming.receiveAsFlow()
                        .filterIsInstance<Frame.Text>()
                        .map { converter.deserialize<SyncEvent>(it) }
                        .collect { trySend(it) }
                }

                awaitClose {
                    job.cancel()
                    this@webSocket.cancel()
                }
            }
        } catch (e: Throwable) {
            when (e) {
                is CancellationException -> Unit
                is IOException, is WebSocketException -> throw WebSocketConnectionDroppedException()
                else -> throw WebSocketConnectionFailedException()
            }
        }
    }.catch { throw it }

}
