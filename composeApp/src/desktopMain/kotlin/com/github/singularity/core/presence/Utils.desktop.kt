package com.github.singularity.core.presence

import com.bfo.zeroconf.Service
import com.github.singularity.core.shared.model.LocalServer
import java.net.Inet4Address
import java.net.NetworkInterface
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo

sealed interface MdnsEvent {
    val server: LocalServer

    data class Resolved(override val server: LocalServer) : MdnsEvent
    data class Removed(override val server: LocalServer) : MdnsEvent
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

fun Service.toServer() = LocalServer(
    ip = addresses.firstOrNull {
        it is Inet4Address && !it.isLoopbackAddress
    }?.hostAddress ?: "Unknown",
    deviceOs = text["deviceOs"] ?: "Unknown",
    deviceName = text["deviceName"] ?: "Unknown",
    deviceId = text["deviceId"] ?: "Unknown",
    syncGroupName = text["syncGroupName"] ?: "Unknown",
    syncGroupId = text["syncGroupId"] ?: "Unknown",
)

fun getJmdns(): JmDNS {
    val wifiKeywords = listOf("wi-fi", "wlan", "wireless", "airport", "en0")
    val ethernetKeywords = listOf("ethernet", "eth", "enp", "en1")

    fun findByKeywords(keywords: List<String>) =
        NetworkInterface.getNetworkInterfaces().asSequence()
            .filter { it.isUp && !it.isLoopback && !it.isVirtual }
            .filter { networkInterface ->
                keywords.any { keyword ->
                    networkInterface.displayName.contains(keyword, true) ||
                            networkInterface.name.contains(keyword, true)
                }
            }
            .flatMap { it.inetAddresses.toList().filterIsInstance<Inet4Address>() }
            .firstOrNull()

    val address = findByKeywords(wifiKeywords) ?: findByKeywords(ethernetKeywords)
    ?: NetworkInterface.getNetworkInterfaces().asSequence()
        .filter { it.isUp && !it.isLoopback && !it.isVirtual }
        .flatMap { it.inetAddresses.asSequence() }
        .filterIsInstance<Inet4Address>()
        .firstOrNull()

    return JmDNS.create(address)
}

