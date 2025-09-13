package com.github.singularity.core.broadcast

import com.appstractive.dnssd.publishService
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.getDeviceName
import com.github.singularity.core.shared.model.HostedSyncGroup
import com.github.singularity.core.shared.os
import com.github.singularity.core.shared.platform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NSDDeviceBroadcastService(
    private val preferencesRepo: PreferencesRepository,
) : DeviceBroadcastService {

    private var scope: CoroutineScope? = null

    override suspend fun startBroadcast(group: HostedSyncGroup) {
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        val deviceId = preferencesRepo.preferences.first().deviceId

        scope?.launch {
            publishService(
                type = MDNS_SERVICE_TYPE,
                name = getServiceName(group),
            ) {
                port = SERVER_PORT
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

    override suspend fun stopBroadcast() {
        scope?.cancel()
    }

}
