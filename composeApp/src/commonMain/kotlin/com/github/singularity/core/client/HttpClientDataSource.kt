package com.github.singularity.core.client

import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.models.Node
import com.github.singularity.models.http.PairRequestResponse

interface HttpClientDataSource {

    suspend fun sendPairRequest(server: LocalServer, currentDevice: Node): PairRequestResponse

    fun release()
    
}
