package com.github.singularity.core.mdns

import com.appstractive.dnssd.DiscoveredService

data class Device(
    val ip: String,
    val deviceName: String,
    val deviceId: String,
)

fun DiscoveredService.toDevice() = Device(
    ip = addresses.first(),
    deviceName = txt["deviceName"]?.decodeToString() ?: "Unknown Device",
    deviceId = txt["deviceId"]?.decodeToString() ?: "Unknown",
)
