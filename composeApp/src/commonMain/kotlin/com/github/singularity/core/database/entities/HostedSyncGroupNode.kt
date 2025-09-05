package com.github.singularity.core.database.entities

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class HostedSyncGroupNode(
    val nodeId: String = Uuid.random().toString(),
    val authToken: String,
    val syncGroupId: String,
    val nodeName: String,
    val nodeOs: String,
)
