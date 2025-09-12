package com.github.singularity.core.broadcast.di

import com.github.singularity.core.broadcast.DeviceBroadcastService
import com.github.singularity.core.broadcast.DeviceDiscoveryService
import com.github.singularity.core.broadcast.JmdnsDeviceBroadcastService
import com.github.singularity.core.broadcast.JmdnsDeviceDiscoveryService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.net.InetAddress
import javax.jmdns.JmDNS

actual val BroadcastModule = module {
    singleOf(::JmdnsDeviceBroadcastService) bind DeviceBroadcastService::class
    singleOf(::JmdnsDeviceDiscoveryService) bind DeviceDiscoveryService::class

    single {
        JmDNS.create(InetAddress.getByName("192.168.1.105"))
    }
}
