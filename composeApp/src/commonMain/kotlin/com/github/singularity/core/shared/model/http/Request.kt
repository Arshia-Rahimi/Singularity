package com.github.singularity.core.shared.model.http

import kotlinx.serialization.Serializable

@Serializable
data class PairRequest(
    val nodeName: String,
    val nodeId: String,
    val nodeOs: String,
    val syncGroupId: String,
    val syncGroupName: String,
)

@Serializable
data class PairCheckRequest(
    val pairRequestId: Long,
    val groupId: String,
    val nodeId: String,
    val nodeOs: String,
    val nodeName: String,
)
