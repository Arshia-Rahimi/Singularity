package com.github.singularity.core.broadcast.di

import com.github.singularity.core.broadcast.DeviceBroadcastService
import com.github.singularity.core.broadcast.DeviceDiscoveryService
import com.github.singularity.core.broadcast.JmdnsDeviceBroadcastService
import com.github.singularity.core.broadcast.JmdnsDeviceDiscoveryService
import com.github.singularity.core.broadcast.MultiJmdnsWrapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.net.Inet4Address
import java.net.NetworkInterface

actual val BroadcastModule = module {
    singleOf(::JmdnsDeviceBroadcastService) bind DeviceBroadcastService::class
    singleOf(::JmdnsDeviceDiscoveryService) bind DeviceDiscoveryService::class

    factory {
        val inetAddresses = buildList {
            NetworkInterface.getNetworkInterfaces().toList().forEach {
                it.inetAddresses.toList().forEach { address ->
                    if (address.isSiteLocalAddress && address is Inet4Address)
                        add(address)
                }
            }
        }

        MultiJmdnsWrapper(*inetAddresses.toTypedArray())
    }
}
