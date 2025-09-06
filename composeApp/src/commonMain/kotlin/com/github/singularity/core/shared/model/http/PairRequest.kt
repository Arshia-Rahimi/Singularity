package com.github.singularity.core.shared.model.http

import com.github.singularity.core.shared.model.Node

data class PairRequest(
    val nodeName: String,
    val nodeId: String,
    val nodeOs: String,
    val syncGroupId: String,
    val syncGroupName: String,
) {
    fun toNode() = Node(
        deviceId = nodeId,
        deviceName = nodeName,
        deviceOs = nodeOs,
    )
}
