package com.github.singularity.core.shared.model

import kotlinx.serialization.Serializable

interface Device {
    val deviceName: String
    val deviceId: String
    val deviceOs: String
}

@Serializable
class Node(
    override val deviceName: String,
    override val deviceId: String,
    override val deviceOs: String,
) : Device

@Serializable
data class LocalServer(
    override val deviceName: String,
    override val deviceId: String,
    override val deviceOs: String,
    val ip: String,
    val syncGroupName: String,
    val syncGroupId: String,
) : Device
