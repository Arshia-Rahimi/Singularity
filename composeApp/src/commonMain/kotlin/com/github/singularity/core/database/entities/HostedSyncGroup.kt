package com.github.singularity.core.database.entities

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class HostedSyncGroup @OptIn(ExperimentalUuidApi::class) constructor(
    val hostedSyncGroupId: String = Uuid.random().toString(),
    val name: String,
    val isDefault: Boolean,
    val nodes: List<HostedSyncGroupNode>,
)
