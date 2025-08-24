package com.github.singularity.core.discover

import com.appstractive.dnssd.DiscoveredService

sealed interface Device {
    val ip: String
    val deviceName: String
    val deviceId: String
    val deviceOs: String
}

class Node(
    override val ip: String,
    override val deviceName: String,
    override val deviceId: String,
    override val deviceOs: String
) : Device

data class Server(
    override val ip: String,
    override val deviceName: String,
    override val deviceId: String,
    override val deviceOs: String,
    val syncGroupName: String,
    val syncGroupId: String,
) : Device

fun DiscoveredService.toServer() = Server(
    ip = addresses.first(),
    deviceName = txt["deviceName"]?.decodeToString() ?: "Unknown Device",
    deviceId = txt["deviceId"]?.decodeToString() ?: "Unknown",
    syncGroupName = txt["syncGroupName"]?.decodeToString() ?: "Unknown",
    deviceOs = txt["deviceOs"]?.decodeToString() ?: "Unknown",
    syncGroupId = txt["SyncGroupId"]?.decodeToString() ?: "Unknown",
)
