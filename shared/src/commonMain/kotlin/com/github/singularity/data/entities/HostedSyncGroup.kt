package com.github.singularity.data.entities

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class HostedSyncGroup(
    val name: String,
    val hostedSyncGroupId: String = Uuid.random().toString(),
    val isDefault: Boolean = false,
    val nodes: List<HostedSyncGroupNode> = emptyList(),
)
