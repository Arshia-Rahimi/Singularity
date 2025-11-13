package com.github.singularity.core.broadcast

//import com.github.singularity.core.data.PreferencesRepository
//import com.github.singularity.core.shared.SERVER_PORT
//import com.github.singularity.core.shared.deviceName
//import com.github.singularity.core.shared.model.HostedSyncGroup
//import com.github.singularity.core.shared.os
//import com.github.singularity.core.shared.platform
//import kotlinx.coroutines.flow.first
//import javax.jmdns.ServiceInfo
//
//class JmdnsDeviceBroadcastService(
//    private val preferencesRepo: PreferencesRepository,
//) : DeviceBroadcastService {
//
//    private val jmdns = getJmdns()
//
//    override suspend fun startBroadcast(group: HostedSyncGroup) {
//        val deviceId = preferencesRepo.preferences.first().deviceId
//
//        val service = ServiceInfo.create(
//            MDNS_SERVICE_TYPE,
//            getServiceName(group),
//	        SERVER_PORT,
//            0,
//            0,
//            mapOf(
//                "deviceName" to deviceName,
//                "deviceId" to deviceId,
//                "devicePlatform" to platform,
//                "deviceOs" to os,
//                "syncGroupName" to group.name,
//                "syncGroupId" to group.hostedSyncGroupId,
//            ),
//        )
//
//        jmdns.registerService(service)
//    }
//
//    override suspend fun stopBroadcast() {
//        jmdns.unregisterAllServices()
//    }
//
//}
