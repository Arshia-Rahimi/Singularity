package com.github.singularity.core.syncservice

import kotlinx.serialization.Serializable

@Serializable
data class SyncEvent(
    val pluginName: String,
    val data: SyncEventData,
)

interface SyncEventData
