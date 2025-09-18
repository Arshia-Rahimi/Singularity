package com.github.singularity.core.shared.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

fun Channel<Unit>.sendPulse() = trySend(Unit)

fun MutableSharedFlow<Unit>.trySendPulse() = tryEmit(Unit)

fun MutableSharedFlow<Unit>.sendPulse(scope: CoroutineScope) {
    scope.launch { emit(Unit) }
}

fun <T> Flow<T>.onFirst(action: suspend (T) -> Unit): Flow<T> {
    var isFirst = true
    return onEach {
        if (isFirst) {
            isFirst = false
            action(it)
        }
    }
}

context(viewModel: ViewModel)
fun <T> Flow<T>.stateInWhileSubscribed(initialValue: T) =
    stateIn(viewModel.viewModelScope, SharingStarted.WhileSubscribed(5000), initialValue)

fun <T> Flow<T>.stateInWhileSubscribed(initialValue: T, scope: CoroutineScope) =
    stateIn(scope, SharingStarted.WhileSubscribed(5000), initialValue)

fun <T> Flow<T>.shareInWhileSubscribed(scope: CoroutineScope, replay: Int = 0) =
    shareIn(scope, SharingStarted.WhileSubscribed(5000), replay)
