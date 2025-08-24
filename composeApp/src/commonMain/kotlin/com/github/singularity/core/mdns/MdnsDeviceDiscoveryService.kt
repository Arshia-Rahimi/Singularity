package com.github.singularity.core.mdns

import com.appstractive.dnssd.DiscoveryEvent
import com.appstractive.dnssd.discoverServices
import com.appstractive.dnssd.publishService
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.getDeviceName
import com.github.singularity.core.shared.model.SyncGroup
import com.github.singularity.core.shared.os
import com.github.singularity.core.shared.platform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.runningFold

class MdnsDeviceDiscoveryService(
    private val preferencesRepo: PreferencesRepository,
) : DeviceDiscoveryService {

    override fun discoverServers() = discoverServices(MDNS_SERVICE_TYPE)
        .runningFold(mutableListOf<Server>()) { list, newServer ->
            when (newServer) {
                is DiscoveryEvent.Discovered -> newServer.resolve()
                is DiscoveryEvent.Resolved -> list + newServer.service.toServer()

                is DiscoveryEvent.Removed -> list.removeAll { item ->
                    item.deviceId == newServer.service.toServer().deviceId
                }
            }
            return@runningFold list
        }.distinctUntilChanged()

    override suspend fun broadcastServer(group: SyncGroup) {
        val deviceId = preferencesRepo.preferences.first().deviceId

        publishService(
            type = MDNS_SERVICE_TYPE,
            name = MDNS_SERVICE_NAME,
        ) {
            port = MDNS_PORT
            txt = mapOf(
                "deviceName" to getDeviceName(),
                "deviceId" to deviceId,
                "devicePlatform" to platform,
                "deviceOs" to os,
                "syncGroupName" to group.name,
                "syncGroupId" to group.id,
            )
        }
    }

}
