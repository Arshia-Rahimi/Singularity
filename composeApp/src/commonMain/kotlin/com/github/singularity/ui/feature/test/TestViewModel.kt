package com.github.singularity.ui.feature.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.shared.serialization.SyncEvent
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import com.github.singularity.core.sync.SyncEventBridge
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class TestEvent(
    val name: String,
    override val plugin: String = "test",
) : SyncEvent

class TestViewModel(
    private val syncEventBridge: SyncEventBridge,
) : ViewModel() {

    private var c = 1

    val incomingEvents = syncEventBridge.incomingSyncEvents
        .filterIsInstance<TestEvent>()
        .runningFold(emptyList<TestEvent>()) { list, event -> list + event }
        .stateInWhileSubscribed(emptyList())

    fun sendEvent() {
        viewModelScope.launch {
            syncEventBridge.send(TestEvent(c.toString()))
            c++
        }
    }

}
