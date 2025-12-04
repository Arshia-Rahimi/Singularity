package com.github.singularity.core.datasource.network.impl

import com.appstractive.dnssd.DiscoveredService
import com.github.singularity.core.datasource.network.LocalServerDto

fun DiscoveredService.toServer() = LocalServerDto(
    ip = addresses.firstOrNull() ?: "",
    deviceName = txt["deviceName"]?.decodeToString() ?: "Unknown Device",
    deviceId = txt["deviceId"]?.decodeToString() ?: "Unknown",
    syncGroupName = txt["syncGroupName"]?.decodeToString() ?: "Unknown",
    deviceOs = txt["deviceOs"]?.decodeToString() ?: "Unknown",
    syncGroupId = txt["syncGroupId"]?.decodeToString() ?: "Unknown",
)
