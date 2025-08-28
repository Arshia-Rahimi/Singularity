package com.github.singularity.models

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
    override val deviceOs: String
) : Device
