package com.github.singularity.core.shared.model

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class HostedSyncGroupNode(
    val nodeId: String = Uuid.random().toString(),
    val authToken: String,
    val syncGroupId: String,
    val syncGroupName: String,
    val nodeName: String,
    val nodeOs: String,
) {
    fun toNode() = Node(
        deviceName = nodeName,
        deviceOs = nodeOs,
        deviceId = nodeId,
    )
}
