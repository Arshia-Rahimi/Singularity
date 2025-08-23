package com.github.singularity.core.mdns

import com.appstractive.dnssd.DiscoveryEvent
import com.appstractive.dnssd.discoverServices
import com.appstractive.dnssd.publishService
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.getDeviceName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class MdnsDeviceDiscoveryService(
    shouldBroadcastDevice: Boolean,
    private val scope: CoroutineScope,
    private val preferencesRepo: PreferencesRepository,
) : DeviceDiscoveryService {

    override val devices = MutableStateFlow(emptyList<Device>())

    init {
        if (shouldBroadcastDevice) broadcastDevice()
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
                        newList.removeAll { d -> d.deviceId == it.service.toDevice().deviceId }
                        devices.value = newList.toList()
                    }
                }
            }
        }
    }

    fun broadcastDevice() {
        scope.launch {
            val deviceId = preferencesRepo.preferences.first().deviceId.toString()

            publishService(
                type = MDNS_SERVICE_TYPE,
                name = MDNS_SERVICE_NAME,
            ) {
                port = MDNS_PORT
                txt = mapOf(
                    "deviceName" to getDeviceName(),
                    "deviceId" to deviceId,
                )
            }
        }
    }

    override fun release() {
        scope.cancel()
    }

}
