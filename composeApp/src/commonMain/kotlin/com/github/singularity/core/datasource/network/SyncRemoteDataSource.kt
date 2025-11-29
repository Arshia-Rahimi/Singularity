package com.github.singularity.core.datasource.network

import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.http.PairCheckResponse
import com.github.singularity.core.shared.model.http.PairResponse
import com.github.singularity.core.shared.util.Success
import kotlinx.coroutines.flow.Flow

interface SyncRemoteDataSource {

    fun connect(server: LocalServer, token: String): Flow<Success>

    suspend fun sendPairRequest(server: LocalServer, currentDevice: Node): PairResponse

    suspend fun sendPairCheckRequest(server: LocalServer, pairRequestId: Int): PairCheckResponse

}