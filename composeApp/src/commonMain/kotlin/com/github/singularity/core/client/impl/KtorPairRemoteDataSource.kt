package com.github.singularity.core.client.impl

import com.github.singularity.core.client.PairRemoteDataSource
import com.github.singularity.core.shared.HTTP_SERVER_PORT
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

class KtorPairRemoteDataSource : PairRemoteDataSource {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    override suspend fun sendPairRequest(server: LocalServer, currentDevice: Node) =
        client.post("http://${server.ip}:${HTTP_SERVER_PORT}/pair") {
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
        client.post("http://${server.ip}:$HTTP_SERVER_PORT/pairCheck") {
            contentType(ContentType.Application.Json)
            setBody(
                PairCheckRequest(
                    pairRequestId,
                    server.syncGroupId,
                )
            )
        }.body<PairCheckResponse>()

}
