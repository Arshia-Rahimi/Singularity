package com.github.singularity.ui.feature.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.datasource.memory.SyncEventBridge
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.syncservice.plugin.SyncEvent
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.launch

class TestViewModel(
    private val syncEventBridge: SyncEventBridge,
) : ViewModel() {

    private var c = 1

    val incomingEvents = syncEventBridge.incomingSyncEvents
	    .runningFold(emptyList<SyncEvent>()) { list, event -> list + event }
        .stateInWhileSubscribed(emptyList())

    fun sendEvent() {
        viewModelScope.launch {
	        syncEventBridge.send(SyncEvent.Test.TestEvent(c))
            c++
        }
    }

}
