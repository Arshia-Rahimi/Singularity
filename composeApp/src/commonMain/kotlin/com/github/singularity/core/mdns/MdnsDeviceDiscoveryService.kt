package com.github.singularity.core.mdns

import com.appstractive.dnssd.DiscoveryEvent
import com.appstractive.dnssd.discoverServices
import com.appstractive.dnssd.publishService
import com.github.singularity.core.shared.MDNS_PORT
import com.github.singularity.core.shared.MDNS_SERVICE_NAME
import com.github.singularity.core.shared.MDNS_SERVICE_TYPE
import com.github.singularity.core.shared.getDeviceName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MdnsDeviceDiscoveryService(
    shouldPublishDevice: Boolean = true,
    private val scope: CoroutineScope,
) {

    val devices = MutableStateFlow(emptyList<Device>())

    init {
        if (shouldPublishDevice) publishDevice()
        discoverDevices()
    }

    fun discoverDevices() {
        scope.launch {
            discoverServices(MDNS_SERVICE_TYPE).collect {
                when (it) {
                    is DiscoveryEvent.Discovered -> it.resolve()
                    is DiscoveryEvent.Resolved -> devices.value += it.service.toDevice()
                    is DiscoveryEvent.Removed -> {
                        val newList = devices.value.toMutableList()
                        newList.removeAll { d -> d.ip == it.service.addresses.first() }
                        devices.value = newList.toList()
                    }
                }
            }
        }
    }

    fun publishDevice() {
        scope.launch {
            publishService(
                type = MDNS_SERVICE_TYPE,
                name = MDNS_SERVICE_NAME,
            ) {
                port = MDNS_PORT
                txt = mapOf(
                    "deviceName" to getDeviceName(),
                )
            }
        }
    }

    fun release() {
        scope.cancel()
    }

}
