package com.github.singularity.core.datasource.network

import kotlinx.coroutines.flow.StateFlow

interface PairRequestDataSource {

	val requests: StateFlow<List<PairCheckModel>>

	fun add(id: Int, pairRequest: PairRequestDto)

    fun remove(id: Int)

	fun approve(node: NodeModel)

	fun reject(node: NodeModel)

	fun get(id: Int): PairCheckModel?

    fun clear()

}