package com.github.singularity.core.broadcast.nsd

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.net.wifi.WifiManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread


data class DiscoveredService(
    val name: String,
    /** The specific ip addresses to reach the service at. May be empty, if not resolved yet. */
    val addresses: List<String>,
    /** The host to reach the service at. May be empty on the very first discovery. */
    val host: String,
    val type: String,
    val port: Int,
    val txt: Map<String, ByteArray?>,
)

/** A combination of a services name and type to uniquely identify it */
val DiscoveredService.key: String
    get() = "${name}${type}".replace(".", "")

sealed interface DiscoveryEvent {
    val service: DiscoveredService

    /**
     * Emitted when a service was first discovered. Host and addresses may be empty at that point. Use
     * resolve() to get associated ip addresses. They will be delivered via DiscoveryEvent.Resolved.
     */
    data class Discovered(
        override val service: DiscoveredService,
        val resolve: () -> Unit,
    ) : DiscoveryEvent

    /** A service was unpublished from the network. */
    data class Removed(
        override val service: DiscoveredService,
    ) : DiscoveryEvent

    /** Addresses of a service where resolved. */
    data class Resolved(
        override val service: DiscoveredService,
        val resolve: () -> Unit,
    ) : DiscoveryEvent
}

fun discoverServices(type: String): Flow<DiscoveryEvent> = callbackFlow {
    val multicastLock: WifiManager.MulticastLock by lazy {
        if (multicastPermissionGranted()) {
            wifiManager.createMulticastLock("nsdMulticastLock")
                .also { it.setReferenceCounted(true) }
        } else {
            throw RuntimeException("Missing required permission CHANGE_WIFI_MULTICAST_STATE")
        }
    }
    val resolveSemaphore = Semaphore(1)

    multicastLock.acquire()

    fun resolveService(serviceInfo: NsdServiceInfo) {
        thread {
            resolveSemaphore.acquire()
            nsdManager.resolveService(
                serviceInfo,
                object : NsdManager.ResolveListener {
                    override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                        println("onResolveFailed: $errorCode")
                        resolveSemaphore.release()
                    }

                    override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                        resolveSemaphore.release()

                        val service: DiscoveredService =
                            when {
                                VERSION.SDK_INT >= VERSION_CODES.M -> serviceInfo.toCommon()
                                else -> {
                                    val resolvedData =
                                        MDNSDiscover.resolve(
                                            "${serviceInfo.serviceName}${serviceInfo.serviceType}".localQualified,
                                            5000,
                                        )

                                    val txtRecords = resolvedData?.txt?.dict?.toByteMap()

                                    serviceInfo.toCommon(txtRecords)
                                }
                            }

                        trySend(
                            DiscoveryEvent.Resolved(
                                service = service,
                            ) {
                                resolveService(serviceInfo)
                            },
                        )
                    }
                },
            )
        }
    }

    val listener: NsdManager.DiscoveryListener =
        object : NsdManager.DiscoveryListener {
            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                println("onStartDiscoveryFailed($serviceType, $errorCode)")
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                println("onStopDiscoveryFailed($serviceType, $errorCode)")
            }

            override fun onDiscoveryStarted(serviceType: String) {}

            override fun onDiscoveryStopped(serviceType: String) {}

            override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                trySend(
                    DiscoveryEvent.Discovered(
                        service = serviceInfo.toCommon(),
                    ) {
                        resolveService(serviceInfo)
                    },
                )
            }

            override fun onServiceLost(serviceInfo: NsdServiceInfo) {
                trySend(
                    DiscoveryEvent.Removed(
                        service = serviceInfo.toCommon(),
                    ),
                )
            }
        }

    nsdManager.discoverServices(type, NsdManager.PROTOCOL_DNS_SD, listener)

    awaitClose {
        multicastLock.release()
        nsdManager.stopServiceDiscovery(listener)
    }
}

internal fun NsdServiceInfo.toCommon(txt: Map<String, ByteArray?>? = null): DiscoveredService =
    DiscoveredService(
        name = serviceName,
        addresses = getAddresses(),
        host = getHostName() ?: "",
        type = serviceType,
        port = port,
        txt = txt ?: attributes ?: emptyMap(),
    )

private fun NsdServiceInfo.getAddresses(): List<String> {
    if (VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
        return hostAddresses.mapNotNull { it.hostAddress }
    }

    return host?.hostAddress?.let { listOf(it) } ?: emptyList()
}

private fun NsdServiceInfo.getHostName(): String? {
    if (VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
        return hostAddresses.firstOrNull()?.canonicalHostName
    }

    return host?.canonicalHostName
}

fun Map<String?, String?>.toByteMap(): Map<String, ByteArray?> =
    filter { (key, _) -> key != null }.mapKeys { it.key!! }.mapValues { it.value?.toByteArray() }
