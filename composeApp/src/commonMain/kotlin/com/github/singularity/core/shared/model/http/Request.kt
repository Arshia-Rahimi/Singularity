package com.github.singularity.core.shared.model.http

import com.github.singularity.core.shared.model.Node
import kotlinx.serialization.Serializable

@Serializable
data class PairRequest(
    val deviceName: String,
    val deviceId: String,
    val deviceOs: String,
    val syncGroupId: String,
    val syncGroupName: String,
) {
    fun toNode() = Node(
        deviceName = deviceName,
        deviceId = deviceId,
        deviceOs = deviceOs,
    )
}

@Serializable
data class PairCheckRequest(
    val pairRequestId: Int,
    val syncGroupId: String,
)
