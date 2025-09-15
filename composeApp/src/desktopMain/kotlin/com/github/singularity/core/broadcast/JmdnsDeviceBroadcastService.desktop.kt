package com.github.singularity.core.broadcast

import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.HTTP_SERVER_PORT
import com.github.singularity.core.shared.getDeviceName
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.os
import com.github.singularity.core.shared.platform
import kotlinx.coroutines.flow.first
import javax.jmdns.ServiceInfo

class JmdnsDeviceBroadcastService(
    private val jmdns: MultiJmdnsWrapper,
    private val preferencesRepo: PreferencesRepository,
) : DeviceBroadcastService {

    override suspend fun startBroadcast(group: HostedSyncGroup) {
        val deviceId = preferencesRepo.preferences.first().deviceId

        jmdns.registerService { weight ->
            ServiceInfo.create(
                MDNS_SERVICE_TYPE,
                getServiceName(group),
                HTTP_SERVER_PORT,
                weight,
                0,
                mapOf(
                    "deviceName" to getDeviceName(),
                    "deviceId" to deviceId,
                    "devicePlatform" to platform,
                    "deviceOs" to os,
                    "syncGroupName" to group.name,
                    "syncGroupId" to group.hostedSyncGroupId,
                ),
            )
        }
    }

    override suspend fun stopBroadcast() {
        jmdns.unregisterAllServices()
    }

}
