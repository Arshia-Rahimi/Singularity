package com.github.singularity.core.datasource.network

import com.github.singularity.core.shared.util.Success
import kotlinx.coroutines.flow.Flow

interface SyncRemoteDataSource {

	fun connect(server: LocalServerModel, token: String): Flow<Success>

	suspend fun sendPairRequest(server: LocalServerModel, currentDevice: NodeModel): PairResponseDto

	suspend fun sendPairCheckRequest(
		server: LocalServerModel,
		pairRequestId: Int
	): PairCheckResponseDto

}
