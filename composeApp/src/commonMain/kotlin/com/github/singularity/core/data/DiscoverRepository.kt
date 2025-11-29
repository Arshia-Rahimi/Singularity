package com.github.singularity.core.data

import com.github.singularity.core.shared.model.LocalServer
import kotlinx.coroutines.flow.Flow

interface DiscoverRepository {

    val discoveredServers: Flow<List<LocalServer>>

    suspend fun sendPairRequest(server: LocalServer)

    suspend fun removeAllDefaults()

}
