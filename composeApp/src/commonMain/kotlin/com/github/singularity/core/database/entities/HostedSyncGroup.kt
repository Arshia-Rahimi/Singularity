package com.github.singularity.core.database.entities

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class HostedSyncGroup @OptIn(ExperimentalUuidApi::class) constructor(
    val name: String,
    val hostedSyncGroupId: String = Uuid.random().toString(),
    val isDefault: Boolean = false,
    val nodes: List<HostedSyncGroupNode> = emptyList(),
)
