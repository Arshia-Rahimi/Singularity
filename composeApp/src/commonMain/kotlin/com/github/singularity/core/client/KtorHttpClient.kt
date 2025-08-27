package com.github.singularity.core.client

import com.github.singularity.SERVER_PORT
import com.github.singularity.core.mdns.Server
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.serialization.kotlinx.json.json

class KtorHttpClient {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun sendPairRequest(server: Server) = client.submitForm(
        url = "${server.ip}:$SERVER_PORT/pair",
//        formParameters = currentDevice.toFormParameters(),
    )

    fun release() {
        client.close()
    }

}
