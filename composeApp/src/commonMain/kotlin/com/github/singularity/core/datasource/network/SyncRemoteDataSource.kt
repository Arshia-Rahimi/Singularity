package com.github.singularity.core.datasource.network

import com.github.singularity.core.shared.util.Success
import kotlinx.coroutines.flow.Flow

interface SyncRemoteDataSource {

	fun connect(server: LocalServerDto, token: String): Flow<Success>

	suspend fun sendPairRequest(server: LocalServerDto, currentDevice: NodeDto): PairResponseDto

	suspend fun sendPairCheckRequest(
		server: LocalServerDto,
		pairRequestId: Int
	): PairCheckResponseDto

}
