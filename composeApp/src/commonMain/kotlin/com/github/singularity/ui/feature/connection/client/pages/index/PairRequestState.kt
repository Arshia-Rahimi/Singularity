package com.github.singularity.ui.feature.connection.client.pages.index

import com.github.singularity.ui.feature.connection.client.DiscoveredServer

sealed interface PairRequestState {
    data object Idle : PairRequestState
    data class Error(val message: String) : PairRequestState
	data class Awaiting(val server: DiscoveredServer) : PairRequestState
	data class Success(val server: DiscoveredServer) : PairRequestState
}