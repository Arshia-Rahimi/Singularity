package com.github.singularity.core.broadcast.nsd

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.net.InetAddress
import java.net.NetworkInterface
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class NetServiceRegisterException(message: String) : RuntimeException(message)


class NetServiceConfig {
    /** the port of the provided service */
    var port: Int = 0

    /** the priority of the created DNS entry (only applied on JVM) */
    var priority: Int = 0

    /** the weight of the created DNS entry relative to priority (only applied on JVM) */
    var weight: Int = 1

    /** the address to advertise (if null, let the platform decide) */
    var addresses: List<String>? = null

    /** optional attributes to publish */
    var txt: Map<String, String> = emptyMap()
}

/**
 * Create a new DNS-SD to publish on the network. Make sure to call this on the main thread.
 *
 * @param type the service type (e.g. _example._tcp)
 * @param name the name of the service to publish
 * @return the platform instance of the service to register or unregister
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc6763">RFC-6763</a>
 */
fun netService(
    type: String,
    name: String,
    configure: NetServiceConfig.() -> Unit = {},
): AndroidNetService {
    val config = NetServiceConfig().apply(configure)
    return createNetService(
        type = type,
        name = name,
        port = config.port,
        priority = config.priority,
        weight = config.weight,
        addresses = config.addresses,
        txt = config.txt,
    )
}

/**
 * Create a new DNS-SD to publish on the network. The service will stay published until the calling
 * coroutine scope is cancelled.
 *
 * @param type the service type (e.g. _example._tcp)
 * @param name the name of the service to publish
 * @return the platform instance of the service to register or unregister
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc6763">RFC-6763</a>
 */
suspend fun publishService(
    type: String,
    name: String,
    timeoutInMs: Long = 10000,
    configure: NetServiceConfig.() -> Unit = {},
): Nothing =
    withContext(Dispatchers.Main) {
        val service =
            netService(
                type = type,
                name = name,
                configure = configure,
            )

        try {
            service.register(timeoutInMs = timeoutInMs)
            awaitCancellation()
        } finally {
            withContext(NonCancellable) { service.unregister() }
        }
    }

class AndroidNetService(
    private val nativeService: NsdServiceInfo,
) : NsdManager.RegistrationListener {

    val name: String = nativeService.serviceName

    val domain: String = nativeService.host.canonicalHostName

    val type: String = nativeService.serviceType

    val port: Int = nativeService.port

    val isRegistered: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var pendingRegister: Continuation<Unit>? = null
    private var pendingUnregister: Continuation<Unit>? = null

    private val mutex = Mutex()

    suspend fun register(timeoutInMs: Long) =
        mutex.withLock {
            if (isRegistered.value) {
                return
            }

            try {
                withTimeout(timeoutInMs) {
                    suspendCancellableCoroutine<Unit> {
                        pendingRegister = it
                        nsdManager.registerService(
                            nativeService,
                            NsdManager.PROTOCOL_DNS_SD,
                            this@AndroidNetService,
                        )
                    }
                }
            } catch (ex: TimeoutCancellationException) {
                pendingRegister = null
                throw NetServiceRegisterException("NsdServiceInfo register timeout")
            }
        }

    suspend fun unregister() =
        mutex.withLock {
            if (!isRegistered.value) {
                return
            }

            suspendCancellableCoroutine {
                pendingUnregister = it
                nsdManager.unregisterService(this)
            }
        }

    override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
        isRegistered.value = true
        pendingRegister?.let {
            it.resume(Unit)
            pendingRegister = null
        }
    }

    override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
        pendingRegister?.let {
            pendingRegister = null
            it.resumeWithException(
                NetServiceRegisterException("NsdServiceInfo register error $errorCode")
            )
        }
    }

    override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {
        isRegistered.value = false
        pendingUnregister?.let {
            pendingUnregister = null
            it.resume(Unit)
        }
    }

    override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
        pendingUnregister?.let {
            pendingUnregister = null
            it.resumeWithException(
                NetServiceRegisterException("NsdServiceInfo unregister error $errorCode")
            )
        }
    }
}

fun createNetService(
    type: String,
    name: String,
    port: Int,
    priority: Int,
    weight: Int,
    addresses: List<String>?,
    txt: Map<String, String>
): AndroidNetService =
    AndroidNetService(
        nativeService =
            NsdServiceInfo().apply {
                serviceName = name
                serviceType = type.stripLocal
                if (VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    hostAddresses =
                        addresses?.map { InetAddress.getByName(it) } ?: getLocalAddresses()
                } else {
                    host =
                        addresses?.firstOrNull()?.let { InetAddress.getByName(it) }
                            ?: getLocalAddresses().firstOrNull()
                }
                this.port = port
                txt.forEach { setAttribute(it.key, it.value) }
            },
    )

private fun getLocalAddresses(): List<InetAddress> = buildList {
    val interfaces = NetworkInterface.getNetworkInterfaces()

    for (netInterface in interfaces) {
        addAll(
            netInterface.inetAddresses.toList().filter { it.isSiteLocalAddress }.toList(),
        )
    }
}
