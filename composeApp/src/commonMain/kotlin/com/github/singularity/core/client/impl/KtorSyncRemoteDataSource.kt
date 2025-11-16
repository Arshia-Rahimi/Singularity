package com.github.singularity.core.client.impl

import com.github.singularity.core.client.SyncRemoteDataSource
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.http.PairCheckRequest
import com.github.singularity.core.shared.model.http.PairCheckResponse
import com.github.singularity.core.shared.model.http.PairRequest
import com.github.singularity.core.shared.model.http.PairResponse
import com.github.singularity.core.sync.SyncEvent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.serialization.serialize
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class KtorSyncRemoteDataSource : SyncRemoteDataSource {

    var wsSession: DefaultClientWebSocketSession? = null
        private set

    private val client = HttpClient(CIO) {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }

        install(ContentNegotiation) {
            json()
        }
    }

    override suspend fun connect(server: LocalServer, token: String) = callbackFlow {
        client.webSocket(
            host = server.ip,
            port = SERVER_PORT,
            path = "/sync",
            request = {
                header(HttpHeaders.Authorization, "Bearer $token")
            },
        ) {
            val converter = converter ?: run {
                close()
                return@webSocket
            }

            val receiveJob = launch {
                try {
                    incoming.consumeAsFlow()
                        .filterIsInstance<Frame.Text>()
                        .map { converter.deserialize<SyncEvent>(it) }
                        .collect(::send)
                } catch (_: Exception) {
                    close()
                }
            }

            launch {
                try {
                    closeReason.await()
                    close()
                } catch (_: Exception) {
                    close()
                }
            }

            awaitClose {
                receiveJob.cancel()
            }

        }
    }

    override suspend fun send(event: SyncEvent) {
        val session = wsSession ?: return
        val converter = session.converter ?: return
        session.send(converter.serialize(event))
    }

    override suspend fun sendPairRequest(server: LocalServer, currentDevice: Node) =
        client.post("http://${server.ip}:$SERVER_PORT/pair") {
            contentType(ContentType.Application.Json)
            setBody(
                PairRequest(
                    deviceName = currentDevice.deviceName,
                    deviceId = currentDevice.deviceId,
                    deviceOs = currentDevice.deviceOs,
                    syncGroupName = server.syncGroupName,
                    syncGroupId = server.syncGroupId,
                )
            )
        }.body<PairResponse>()

    override suspend fun sendPairCheckRequest(server: LocalServer, pairRequestId: Int) =
        client.get("http://${server.ip}:$SERVER_PORT/pairCheck") {
            contentType(ContentType.Application.Json)
            setBody(
                PairCheckRequest(
                    pairRequestId = pairRequestId,
                    syncGroupId = server.syncGroupId,
                )
            )
        }.body<PairCheckResponse>()

}
