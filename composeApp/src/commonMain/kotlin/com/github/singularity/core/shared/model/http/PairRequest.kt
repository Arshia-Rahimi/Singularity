package com.github.singularity.core.shared.model.http

data class PairRequest(
    val nodeName: String,
    val nodeId: String,
    val nodeOs: String,
    val syncGroupId: String,
    val syncGroupName: String,
)
