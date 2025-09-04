package com.github.singularity.core.mdns.di

import com.github.singularity.core.mdns.DeviceBroadcastService
import com.github.singularity.core.mdns.DeviceDiscoveryService
import com.github.singularity.core.mdns.impl.MdnsDeviceBroadcastService
import com.github.singularity.core.mdns.impl.MdnsDeviceDiscoveryService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val MdnsModule = module {
    singleOf(::MdnsDeviceDiscoveryService) bind DeviceDiscoveryService::class
    singleOf(::MdnsDeviceBroadcastService) bind DeviceBroadcastService::class
}
