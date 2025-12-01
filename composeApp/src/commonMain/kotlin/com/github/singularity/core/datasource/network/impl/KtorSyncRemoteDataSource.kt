package com.github.singularity.core.datasource.network.impl

import com.github.singularity.core.datasource.memory.SyncEventBridge
import com.github.singularity.core.datasource.network.SyncRemoteDataSource
import com.github.singularity.core.log.Logger
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.http.PairCheckRequest
import com.github.singularity.core.shared.model.http.PairCheckResponse
import com.github.singularity.core.shared.model.http.PairRequest
import com.github.singularity.core.shared.model.http.PairResponse
import com.github.singularity.core.shared.util.Success
import com.github.singularity.core.syncservice.plugin.SyncEvent
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
import kotlinx.serialization.json.Json

class KtorSyncRemoteDataSource(
	private val syncEventBridge: SyncEventBridge,
	private val logger: Logger,
) : SyncRemoteDataSource {

    private var client = HttpClient(CIO) {
	    install(WebSockets.Plugin) {
		    contentConverter =
			    KotlinxWebsocketSerializationConverter(Json.Default)
		    maxFrameSize = Long.MAX_VALUE
	    }

	    install(ContentNegotiation) {
		    json()
	    }
    }

    override fun connect(server: LocalServer, token: String) = flow {
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
                        logger.e(this::class.simpleName, "outgoingEvent error", e)
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
                    logger.e(this::class.simpleName, "incomingEvent error", e)
                }
            }

            joinAll(receiveJob, sendJob)

	    }
    }

    override suspend fun sendPairRequest(server: LocalServer, currentDevice: Node) =
        client.post("http://${server.ip}:${SERVER_PORT}/api/pair") {
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

    override suspend fun sendPairCheckRequest(
	    server: LocalServer,
	    pairRequestId: Int
    ) = client.get("http://${server.ip}:${SERVER_PORT}/api/pairCheck") {
        contentType(ContentType.Application.Json)
        setBody(
	        PairCheckRequest(
		        pairRequestId = pairRequestId,
		        syncGroupId = server.syncGroupId,
	        )
        )
    }.body<PairCheckResponse>()

}