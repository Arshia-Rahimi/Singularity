package com.github.singularity.core.client

import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.model.Node
import com.github.singularity.core.shared.model.http.PairCheckResponse
import com.github.singularity.core.shared.model.http.PairResponse
import com.github.singularity.core.sync.SyncEvent
import kotlinx.coroutines.flow.Flow

interface SyncRemoteDataSource {

    suspend fun connect(server: LocalServer, token: String)

    suspend fun disconnect()

    fun incomingEventsFlow(): Flow<SyncEvent>

    suspend fun send(event: SyncEvent)

    suspend fun sendPairRequest(server: LocalServer, currentDevice: Node): PairResponse

    suspend fun sendPairCheckRequest(server: LocalServer, pairRequestId: Int): PairCheckResponse

}
