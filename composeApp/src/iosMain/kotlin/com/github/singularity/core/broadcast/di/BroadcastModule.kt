package com.github.singularity.core.broadcast.di

import com.github.singularity.core.broadcast.DeviceBroadcastService
import com.github.singularity.core.broadcast.DeviceDiscoveryService
import com.github.singularity.core.broadcast.NSDDeviceBroadcastService
import com.github.singularity.core.broadcast.NSDDeviceDiscoverService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val BroadcastModule = module {
    singleOf(::NSDDeviceBroadcastService) bind DeviceBroadcastService::class
    singleOf(::NSDDeviceDiscoverService) bind DeviceDiscoveryService::class
}
