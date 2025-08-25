package com.github.singularity.core.client

import com.github.singularity.core.mdns.MDNS_PORT
import com.github.singularity.core.mdns.Server
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.serialization.kotlinx.json.json

class KtorHttpClient {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun sendPairRequest(server: Server) =
        client.post("${server.ip}:$MDNS_PORT") {
            // todo
        }

    fun release() {
        client.close()
    }

}
