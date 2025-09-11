package com.github.singularity.core.broadcast

import com.appstractive.dnssd.DiscoveredService
import com.github.singularity.core.shared.getDeviceName
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.model.LocalServer
import com.github.singularity.core.shared.platform

const val MDNS_SERVICE_TYPE = "_singularity_sync._tcp"

fun getServiceName(group: HostedSyncGroup) =
    "Singularity-$platform-${getDeviceName()}-${group.hostedSyncGroupId}.${MDNS_SERVICE_TYPE}"

fun DiscoveredService.toServer() = LocalServer(
    ip = addresses.firstOrNull() ?: "",
    deviceName = txt["deviceName"]?.decodeToString() ?: "Unknown Device",
    deviceId = txt["deviceId"]?.decodeToString() ?: "Unknown",
    syncGroupName = txt["syncGroupName"]?.decodeToString() ?: "Unknown",
    deviceOs = txt["deviceOs"]?.decodeToString() ?: "Unknown",
    syncGroupId = txt["SyncGroupId"]?.decodeToString() ?: "Unknown",
)
