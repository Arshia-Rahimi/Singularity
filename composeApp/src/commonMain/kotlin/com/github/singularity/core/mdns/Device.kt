package com.github.singularity.core.mdns

import com.appstractive.dnssd.DiscoveredService

data class Device(
    val ip: String,
    val deviceName: String,
)

fun DiscoveredService.toDevice() = Device(
    ip = addresses.first(),
    deviceName = txt["deviceName"]?.decodeToString() ?: "Unknown Device",
)
