package com.github.singularity.core.mdns.impl

import com.appstractive.dnssd.publishService
import com.github.singularity.HTTP_SERVER_PORT
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.database.entities.HostedSyncGroup
import com.github.singularity.core.mdns.DeviceBroadcastService
import com.github.singularity.core.mdns.MDNS_SERVICE_NAME
import com.github.singularity.core.mdns.MDNS_SERVICE_TYPE
import com.github.singularity.core.shared.getDeviceName
import com.github.singularity.core.shared.os
import com.github.singularity.core.shared.platform
import kotlinx.coroutines.flow.first

class MdnsDeviceBroadcastService(
    private val preferencesRepo: PreferencesRepository
) : DeviceBroadcastService {

    override suspend fun broadcastServer(group: HostedSyncGroup) {
        val deviceId = preferencesRepo.preferences.first().deviceId

        publishService(
            type = MDNS_SERVICE_TYPE,
            name = MDNS_SERVICE_NAME,
        ) {
            port = HTTP_SERVER_PORT
            txt = mapOf(
                "deviceName" to getDeviceName(),
                "deviceId" to deviceId,
                "devicePlatform" to platform,
                "deviceOs" to os,
                "syncGroupName" to group.name,
                "syncGroupId" to group.hostedSyncGroupId,
            )
        }
    }

}
