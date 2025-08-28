package com.github.singularity.core.mdns.di

import com.github.singularity.core.mdns.DeviceBroadcastService
import com.github.singularity.core.mdns.DeviceDiscoveryService
import com.github.singularity.core.mdns.impl.MdnsDeviceBroadcastService
import com.github.singularity.core.mdns.impl.MdnsDeviceDiscoveryService
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val MdnsModule = module {
    factoryOf(::MdnsDeviceDiscoveryService) bind DeviceDiscoveryService::class
    factoryOf(::MdnsDeviceBroadcastService) bind DeviceBroadcastService::class
}
