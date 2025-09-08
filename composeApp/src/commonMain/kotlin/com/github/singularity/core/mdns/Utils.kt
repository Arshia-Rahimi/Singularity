package com.github.singularity.core.mdns

import com.appstractive.dnssd.DiscoveredService
import com.github.singularity.core.shared.getDeviceName
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.platform

val MDNS_SERVICE_NAME = "Singularity-$platform-${getDeviceName()}"
const val MDNS_SERVICE_TYPE = "_sync_service._tcp"

fun DiscoveredService.toServer() = LocalServer(
    ip = addresses.first(),
    deviceName = txt["deviceName"]?.decodeToString() ?: "Unknown Device",
    deviceId = txt["deviceId"]?.decodeToString() ?: "Unknown",
    syncGroupName = txt["syncGroupName"]?.decodeToString() ?: "Unknown",
    deviceOs = txt["deviceOs"]?.decodeToString() ?: "Unknown",
    syncGroupId = txt["SyncGroupId"]?.decodeToString() ?: "Unknown",
)
