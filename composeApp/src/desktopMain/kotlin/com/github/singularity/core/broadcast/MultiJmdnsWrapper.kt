package com.github.singularity.core.broadcast

import java.net.InetAddress
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo

class MultiJmdnsWrapper(
    vararg inetAddress: InetAddress,
) {

    private val jmdns = inetAddress.map {
        JmDNS.create(it)
    }

    fun registerService(serviceInfo: ServiceInfo) {
        jmdns.forEach { it.registerService(serviceInfo) }
    }

    fun unregisterAllServices() {
        jmdns.forEach { it.unregisterAllServices() }
    }

}
