package com.github.singularity.core.datasource.network

data class PairCheckModel(
	val requestId: Int,
	val node: NodeDto,
	val status: PairStatus = PairStatus.Awaiting,
)
