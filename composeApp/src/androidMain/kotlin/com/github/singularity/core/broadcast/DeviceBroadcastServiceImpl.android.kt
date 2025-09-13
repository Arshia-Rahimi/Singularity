package com.github.singularity.core.broadcast

import com.appstractive.dnssd.NetService
import com.appstractive.dnssd.createNetService
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.HTTP_SERVER_PORT
import com.github.singularity.core.shared.getDeviceName
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.os
import com.github.singularity.core.shared.platform
import kotlinx.coroutines.flow.first

class DeviceBroadcastServiceImpl(
    private val preferencesRepo: PreferencesRepository,
) : DeviceBroadcastService {

    private var service: NetService? = null

    override suspend fun startBroadcast(group: HostedSyncGroup) {
        val deviceId = preferencesRepo.preferences.first().deviceId

        service = createNetService(
            type = MDNS_SERVICE_TYPE,
            name = getServiceName(group),
            port = HTTP_SERVER_PORT,
            priority = 0,
            weight = 0,
            addresses = null,
            txt = mapOf(
                "deviceName" to getDeviceName(),
                "deviceId" to deviceId,
                "devicePlatform" to platform,
                "deviceOs" to os,
                "syncGroupName" to group.name,
                "syncGroupId" to group.hostedSyncGroupId,
            )
        )
        service?.register()
    }

    override suspend fun stopBroadcast() {
        if (service?.isRegistered?.value ?: false) {
            service?.unregister()
        }
    }

}
