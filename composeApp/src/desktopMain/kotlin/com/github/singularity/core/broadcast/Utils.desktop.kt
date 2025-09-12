package com.github.singularity.core.broadcast

import com.github.singularity.core.shared.model.LocalServer
import java.net.Inet4Address
import javax.jmdns.ServiceInfo

sealed interface JmdnsEvent {
    val server: LocalServer

    data class Resolved(override val server: LocalServer) : JmdnsEvent
    data class Removed(override val server: LocalServer) : JmdnsEvent
}

fun ServiceInfo.toServer() = LocalServer(
    ip = inetAddresses.firstOrNull {
        it is Inet4Address && !it.isLoopbackAddress
    }?.hostAddress ?: "Unknown",
    deviceOs = getPropertyString("deviceOs"),
    deviceName = getPropertyString("deviceName"),
    deviceId = getPropertyString("deviceId"),
    syncGroupName = getPropertyString("syncGroupName"),
    syncGroupId = getPropertyString("syncGroupId"),
)
