package com.github.singularity.ui.feature.main

import androidx.lifecycle.ViewModel

class MainViewModel(
//    private val connectionRepo: ClientConnectionRepository,
) : ViewModel() {

//    private val connectionState = connectionRepo.connectionState
//        .stateInWhileSubscribed(ConnectionState.NoDefaultServer)
//
//    val uiState = combine(connectionState) { connectionState ->
//        MainUiState(
//            connectionState = connectionState[0],
//        )
//    }.stateInWhileSubscribed(MainUiState())

    fun execute(intent: MainIntent) {
        when (intent) {
//            is MainIntent.RefreshConnection -> refreshConnection()
            else -> Unit
        }
    }

//    private fun refreshConnection() = connectionRepo.refresh()

}
