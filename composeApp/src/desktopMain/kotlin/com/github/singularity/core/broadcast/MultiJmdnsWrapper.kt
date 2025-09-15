package com.github.singularity.core.broadcast

import java.net.InetAddress
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo
import javax.jmdns.ServiceListener

class MultiJmdnsWrapper(
    vararg inetAddress: InetAddress,
) {

    private val jmdns = inetAddress.map {
        JmDNS.create(it)
    }

    fun registerService(serviceInfo: (Int) -> ServiceInfo) {
        jmdns.forEachIndexed { index, item -> item.registerService(serviceInfo(index)) }
    }

    fun unregisterAllServices() {
        jmdns.forEach { it.unregisterAllServices() }
    }

    fun addServiceListener(serviceType: String, listener: ServiceListener) {
        jmdns.forEach { it.addServiceListener(serviceType, listener) }
    }

    fun removeServiceListener(serviceType: String, listener: ServiceListener) {
        jmdns.forEach { it.removeServiceListener(serviceType, listener) }
    }

}
