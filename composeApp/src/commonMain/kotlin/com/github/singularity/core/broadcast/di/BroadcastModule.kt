package com.github.singularity.core.broadcast.di

import com.github.singularity.core.broadcast.DeviceBroadcastService
import com.github.singularity.core.broadcast.DeviceDiscoveryService
import com.github.singularity.core.broadcast.impl.MdnsDeviceBroadcastService
import com.github.singularity.core.broadcast.impl.MdnsDeviceDiscoveryService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val BroadcastModule = module {
    singleOf(::MdnsDeviceDiscoveryService) bind DeviceDiscoveryService::class
    singleOf(::MdnsDeviceBroadcastService) bind DeviceBroadcastService::class
}
