package com.github.singularity.core.httpclient

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post

class KtorHttpClient(
    serverUrl: String,
    serverPort: Int,
) {
    
    private val client = HttpClient(CIO) {
        defaultRequest {
            url(serverUrl)
            port = serverPort
        }
    }
    
    suspend fun sendPairRequest() {
        client.post("pair") {
        
        }
    }
    
}
