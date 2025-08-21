package com.github.singularity.core.common.compose

import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

fun <T> Flow<List<T>>.collectToSnapShotStateList(
    scope: CoroutineScope,
    sort: (List<T>) -> List<T> = { it },
) =
    mutableStateListOf<T>().apply {
        this@collectToSnapShotStateList.onEach { items ->
            val sortedItems = sort(items)
            clear()
            addAll(sortedItems)
        }.launchIn(scope)
    }

fun AnchoredDraggableState<*>.safeOffset() = if (offset.isNaN()) 0f else offset

@Composable
fun StringResource.getString() = stringResource(this)
