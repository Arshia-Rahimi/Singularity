package com.github.singularity.core.client.http

import com.github.singularity.core.client.HttpClientDataSource
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.models.Node
import com.github.singularity.models.PairRequestResponse
import io.ktor.client.call.body

class KtorHttpClientDataSource(
    private val client: KtorHttpClient,
) : HttpClientDataSource {

    override suspend fun sendPairRequest(server: LocalServer, currentDevice: Node) =
        client.sendPairRequest(server, currentDevice).body<PairRequestResponse>()

    override fun release() {
        client.release()
    }

}
