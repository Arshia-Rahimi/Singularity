package com.github.singularity.models

import kotlinx.serialization.Serializable

interface Device {
    val ip: String
    val deviceName: String
    val deviceId: String
    val deviceOs: String
}

@Serializable
class Node(
    override val ip: String,
    override val deviceName: String,
    override val deviceId: String,
    override val deviceOs: String
) : Device
