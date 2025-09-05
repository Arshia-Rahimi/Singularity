package com.github.singularity.core.shared.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class HostedSyncGroup(
    val name: String,
    val isDefault: Boolean = false,
    val hostedSyncGroupId: String = Uuid.random().toString(),
    val nodes: List<HostedSyncGroupNode> = emptyList(),
)
