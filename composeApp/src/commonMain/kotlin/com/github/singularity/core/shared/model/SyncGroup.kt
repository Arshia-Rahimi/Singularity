package com.github.singularity.core.shared.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class SyncGroup(
    val name: String,
    val id: String = Uuid.random().toString(),
)
