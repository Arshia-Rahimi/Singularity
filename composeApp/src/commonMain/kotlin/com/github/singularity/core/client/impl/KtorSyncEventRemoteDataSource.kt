package com.github.singularity.core.client.impl

import com.github.singularity.core.client.SyncEventRemoteDataSource
import com.github.singularity.core.client.WebSocketConnectionDroppedException
import com.github.singularity.core.client.WebSocketConnectionFailedException
import com.github.singularity.core.shared.WEBSOCKET_SERVER_PORT
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.model.websocket.SyncEvent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.websocket.WebSocketException
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpHeaders
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.serialize
import io.ktor.websocket.Frame
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
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

class KtorSyncEventRemoteDataSource : SyncEventRemoteDataSource {

    private var authToken: String? = null

    val authPlugin = createClientPlugin("AuthPlugin") {
        onRequest { request, _ ->
            authToken?.let {
                request.headers[HttpHeaders.Authorization] = "Bearer $it"
            }
        }
    }

    private val client = HttpClient(CIO) {
        install(authPlugin)
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(
                Json { ignoreUnknownKeys = true }
            )
        }
    }

    private val outgoingEvents = Channel<SyncEvent>(Channel.BUFFERED)

    override fun connect(server: LocalServer, token: String) = callbackFlow {
        authToken = token
        try {
            client.webSocket("ws://${server.ip}:$WEBSOCKET_SERVER_PORT/sync") {
                val converter = converter ?: return@webSocket

                val sender = launch {
                    outgoingEvents.receiveAsFlow()
                        .map { converter.serialize<SyncEvent>(it) }
                        .collect { send(it) }
                }

                val receiver = launch {
                    incoming.receiveAsFlow()
                        .filterIsInstance<Frame.Text>()
                        .map { converter.deserialize<SyncEvent>(it) }
                        .collect { send(it) }
                }

                awaitClose {
                    sender.cancel()
                    receiver.cancel()
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

    override suspend fun send(event: SyncEvent) {
        outgoingEvents.send(event)
    }

}
