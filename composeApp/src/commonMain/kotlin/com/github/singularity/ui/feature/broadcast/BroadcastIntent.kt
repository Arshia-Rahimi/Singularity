package com.github.singularity.ui.feature.broadcast

sealed interface BroadcastIntent {
    data object NavBack : BroadcastIntent
}

