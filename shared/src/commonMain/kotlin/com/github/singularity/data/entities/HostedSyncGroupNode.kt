package com.github.singularity.data.entities

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class HostedSyncGroupNode @OptIn(ExperimentalUuidApi::class) constructor(
    val nodeId: String = Uuid.random().toString(),
    val authToken: String,
    val syncGroupId: String,
)
