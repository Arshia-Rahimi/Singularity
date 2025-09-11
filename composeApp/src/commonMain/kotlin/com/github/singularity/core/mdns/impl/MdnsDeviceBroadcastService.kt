package com.github.singularity.core.mdns.impl

import com.appstractive.dnssd.NetService
import com.appstractive.dnssd.createNetService
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.mdns.DeviceBroadcastService
import com.github.singularity.core.mdns.MDNS_SERVICE_TYPE
import com.github.singularity.core.mdns.getServiceName
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.getDeviceName
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.os
import com.github.singularity.core.shared.platform
import kotlinx.coroutines.flow.first

class MdnsDeviceBroadcastService(
    private val preferencesRepo: PreferencesRepository
) : DeviceBroadcastService {

    private var service: NetService? = null

    override suspend fun startBroadcast(group: HostedSyncGroup) {
        val deviceId = preferencesRepo.preferences.first().deviceId

        service = createNetService(
            type = MDNS_SERVICE_TYPE,
            name = getServiceName(group),
            port = SERVER_PORT,
            addresses = null,
            txt = mapOf(
                "deviceName" to getDeviceName(),
                "deviceId" to deviceId,
                "devicePlatform" to platform,
                "deviceOs" to os,
                "syncGroupName" to group.name,
                "syncGroupId" to group.hostedSyncGroupId,
            ),
        )

        service?.register()
    }

    override suspend fun stopBroadcast() {
        service?.unregister()
    }

}
