package com.github.singularity.core.client.impl

import com.github.singularity.core.client.HttpClientDataSource
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.http.PairCheckRequest
import com.github.singularity.core.shared.model.http.PairCheckResponse
import com.github.singularity.core.shared.model.http.PairRequest
import com.github.singularity.core.shared.model.http.PairResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

class KtorHttpClientDataSource : HttpClientDataSource {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    override suspend fun sendPairRequest(server: LocalServer, currentDevice: Node) =
        client.post("${server.ip}:${SERVER_PORT}/pair") {
            contentType(ContentType.Application.Json)
            setBody(
                PairRequest(
                    nodeName = currentDevice.deviceName,
                    nodeId = currentDevice.deviceId,
                    nodeOs = currentDevice.deviceOs,
                    syncGroupName = server.syncGroupName,
                    syncGroupId = server.syncGroupId,
                )
            )
        }.body<PairResponse>()

    override suspend fun pairCheckRequest(
        server: LocalServer,
        currentDevice: Node,
        pairRequestId: Long
    ) =
        client.post("${server.ip}:$SERVER_PORT/pairCheck") {
            contentType(ContentType.Application.Json)
            setBody(
                PairCheckRequest(
                    pairRequestId,
                    server.syncGroupId,
                    nodeId = currentDevice.deviceId,
                    nodeOs = currentDevice.deviceOs,
                    nodeName = currentDevice.deviceName,
                )
            )
        }.body<PairCheckResponse>()


    override fun release() {
        client.close()
    }

}
