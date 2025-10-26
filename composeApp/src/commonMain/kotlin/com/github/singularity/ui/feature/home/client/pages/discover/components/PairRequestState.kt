package com.github.singularity.ui.feature.home.client.pages.discover.components

import com.github.singularity.core.shared.model.LocalServer

sealed interface PairRequestState {
    data object Idle : PairRequestState
    data class Error(val message: String) : PairRequestState
    data class Awaiting(val server: LocalServer) : PairRequestState
    data class Success(val server: LocalServer) : PairRequestState
}
