package com.github.singularity.core.sync.datasource

import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.http.PairCheckRequest
import com.github.singularity.core.shared.model.http.PairCheckResponse
import com.github.singularity.core.shared.model.http.PairRequest
import com.github.singularity.core.shared.model.http.PairResponse
import com.github.singularity.core.shared.util.Success
import com.github.singularity.core.shared.util.asResult
import com.github.singularity.core.sync.SyncEvent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class KtorSyncRemoteDataSource(
    private val syncEventBridge: SyncEventBridge,
) : SyncRemoteDataSource {

    private val client = HttpClient(CIO) {
        install(WebSockets.Plugin) {
            contentConverter =
                KotlinxWebsocketSerializationConverter(Json.Default)
        }

        install(ContentNegotiation) {
            json()
        }
    }

    override suspend fun connect(server: LocalServer, token: String) = flow {
        client.webSocket(
            host = server.ip,
            port = SERVER_PORT,
            path = "/sync",
            request = {
                header(HttpHeaders.Authorization, "Bearer $token")
            },
        ) {
            emit(Success)
            val converter = converter ?: run {
                close()
                return@webSocket
            }

            launch {
                try {
                    incoming.consumeAsFlow()
                        .filterIsInstance<Frame.Text>()
                        .map { converter.deserialize<SyncEvent>(it) }
                        .collect { syncEventBridge.incomingEventCallback(it) }
                } catch (_: Exception) {
                    close()
                }
            }

            launch {
                try {
                    syncEventBridge.outgoingSyncEvents
                        .map { converter.serialize<SyncEvent>(it) }
                        .collect { send(it) }
                } catch (_: Exception) {
                    close()
                }
            }

        }
    }.asResult(Dispatchers.IO)

    override suspend fun sendPairRequest(server: LocalServer, currentDevice: Node) =
        client.post("http://${server.ip}:${SERVER_PORT}/pair") {
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
        client.get("http://${server.ip}:${SERVER_PORT}/pairCheck") {
            contentType(ContentType.Application.Json)
            setBody(
                PairCheckRequest(
                    pairRequestId = pairRequestId,
                    syncGroupId = server.syncGroupId,
                )
            )
        }.body<PairCheckResponse>()

}