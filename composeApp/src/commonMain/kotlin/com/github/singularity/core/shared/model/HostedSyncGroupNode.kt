package com.github.singularity.core.shared.model

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class HostedSyncGroupNode(
    val deviceId: String = Uuid.random().toString(),
    val deviceName: String,
    val deviceOs: String,
    val authToken: String,
    val syncGroupId: String,
    val syncGroupName: String,
) {
    fun toNode() = Node(
        deviceName = deviceName,
        deviceOs = deviceOs,
        deviceId = deviceId,
    )
}
