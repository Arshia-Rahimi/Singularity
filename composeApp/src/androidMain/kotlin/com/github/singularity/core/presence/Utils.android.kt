package com.github.singularity.core.presence

import com.appstractive.dnssd.DiscoveredService
import com.github.singularity.core.shared.model.LocalServer

fun DiscoveredService.toServer() = LocalServer(
    ip = addresses.firstOrNull() ?: "",
    deviceName = txt["deviceName"]?.decodeToString() ?: "Unknown Device",
    deviceId = txt["deviceId"]?.decodeToString() ?: "Unknown",
    syncGroupName = txt["syncGroupName"]?.decodeToString() ?: "Unknown",
    deviceOs = txt["deviceOs"]?.decodeToString() ?: "Unknown",
    syncGroupId = txt["syncGroupId"]?.decodeToString() ?: "Unknown",
)
