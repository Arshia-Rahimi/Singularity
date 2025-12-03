package com.github.singularity.core.data

import com.github.singularity.core.datasource.network.LocalServerModel
import kotlinx.coroutines.flow.Flow

interface DiscoverRepository {

	val discoveredServers: Flow<List<LocalServerModel>>

	suspend fun sendPairRequest(server: LocalServerModel)

    suspend fun removeAllDefaults()

}
