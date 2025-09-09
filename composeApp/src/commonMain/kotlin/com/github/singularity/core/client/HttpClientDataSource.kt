package com.github.singularity.core.client

import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.http.PairCheckResponse
import com.github.singularity.core.shared.model.http.PairResponse

interface HttpClientDataSource {

    suspend fun sendPairRequest(server: LocalServer, currentDevice: Node): PairResponse

    suspend fun pairCheckRequest(
        server: LocalServer,
        currentDevice: Node,
        pairRequestId: Long
    ): PairCheckResponse

    fun release()
    
}
