package com.github.singularity.core.datasource.network.impl

import com.github.singularity.core.datasource.network.LocalServerDto
import com.github.singularity.core.datasource.network.NodeDto
import com.github.singularity.core.datasource.network.PairCheckRequestDto
import com.github.singularity.core.datasource.network.PairCheckResponseDto
import com.github.singularity.core.datasource.network.PairRequestDto
import com.github.singularity.core.datasource.network.PairResponseDto
import com.github.singularity.core.datasource.network.SyncRemoteDataSource
import com.github.singularity.core.log.Logger
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.util.Success
import com.github.singularity.core.syncservice.events.SyncEvent
import com.github.singularity.core.syncservice.events.SyncEventBridge
import com.github.singularity.core.syncservice.events.syncEventJson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.sendSerialized
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
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class KtorSyncRemoteDataSource(
    private val syncEventBridge: SyncEventBridge,
    private val logger: Logger,
) : SyncRemoteDataSource {

    private var client = HttpClient(CIO) {
        install(WebSockets.Plugin) {
            contentConverter = KotlinxWebsocketSerializationConverter(syncEventJson)
            maxFrameSize = Long.MAX_VALUE
        }

        install(ContentNegotiation) {
            json(syncEventJson)
        }
    }

    override fun connect(server: LocalServerDto, token: String) = flow {
        client.webSocket(
            host = server.ip,
            port = SERVER_PORT,
            path = "/ws/sync",
            request = {
                header(HttpHeaders.Authorization, "Bearer $token")
            },
        ) {
            emit(Success)

            val converter = converter ?: run {
                close()
                return@webSocket
            }

            val sendJob = launch {
                syncEventBridge.outgoingSyncEvents.collect { event ->
                    try {
                        sendSerialized(event)
                    } catch (e: Exception) {
                        logger.e("outgoingEvent error", e)
                    }
                }
            }

            val receiveJob = launch {
                try {
                    incoming.consumeEach { frame ->
                        if (frame !is Frame.Text) return@consumeEach
                        val event = converter.deserialize<SyncEvent>(frame)
                        syncEventBridge.incomingEventCallback(event)
                    }
                } catch (e: Exception) {
                    logger.e("incomingEvent error", e)
                }
            }

            joinAll(receiveJob, sendJob)

        }
    }

    override suspend fun sendPairRequest(server: LocalServerDto, currentDevice: NodeDto) =
        client.post("http://${server.ip}:${SERVER_PORT}/api/pair") {
            contentType(ContentType.Application.Json)
            setBody(
                PairRequestDto(
                    deviceName = currentDevice.deviceName,
                    deviceId = currentDevice.deviceId,
                    deviceOs = currentDevice.deviceOs,
                    syncGroupName = server.syncGroupName,
                    syncGroupId = server.syncGroupId,
                )
            )
        }.body<PairResponseDto>()

    override suspend fun sendPairCheckRequest(
        server: LocalServerDto,
        pairRequestId: Int
    ) = client.get("http://${server.ip}:${SERVER_PORT}/api/pairCheck") {
        contentType(ContentType.Application.Json)
        setBody(
            PairCheckRequestDto(
                pairRequestId = pairRequestId,
                syncGroupId = server.syncGroupId,
            )
        )
    }.body<PairCheckResponseDto>()

}