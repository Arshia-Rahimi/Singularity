package com.github.singularity.core.client.impl

import com.github.singularity.core.client.HttpClientDataSource
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.model.Node
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
                    deviceName = currentDevice.deviceName,
                    deviceId = currentDevice.deviceId,
                    deviceOs = currentDevice.deviceOs,
                    syncGroupName = server.syncGroupName,
                    syncGroupId = server.syncGroupId,
                )
            )
        }.body<PairResponse>()

    override fun release() {
        client.close()
    }

}
