package com.github.singularity.core.data

import com.github.singularity.core.datasource.network.LocalServerDto
import kotlinx.coroutines.flow.Flow

interface DiscoverRepository {

	val discoveredServers: Flow<List<LocalServerDto>>

	suspend fun sendPairRequest(server: LocalServerDto)

    suspend fun removeAllDefaults()

}
