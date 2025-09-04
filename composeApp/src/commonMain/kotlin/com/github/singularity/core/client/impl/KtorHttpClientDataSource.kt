package com.github.singularity.core.client.impl

import com.github.singularity.SERVER_PORT
import com.github.singularity.core.client.HttpClientDataSource
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.models.Node
import com.github.singularity.models.http.PairRequestResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json

class KtorHttpClientDataSource : HttpClientDataSource {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    override suspend fun sendPairRequest(server: LocalServer, currentDevice: Node) =
        client.submitForm(
            url = "${server.ip}:${SERVER_PORT}/pair",
            formParameters = parameters {
                append("deviceName", currentDevice.deviceName)
                append("deviceId", currentDevice.deviceId)
                append("deviceOs", currentDevice.deviceOs)
                append("syncGroupId", server.syncGroupId)
                append("syncGroupName", server.syncGroupName)
            },
        ).body<PairRequestResponse>()

    override fun release() {
        client.close()
    }

}
