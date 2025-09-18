package com.github.singularity.core.broadcast.di

import com.github.singularity.core.broadcast.DeviceBroadcastService
import com.github.singularity.core.broadcast.DeviceDiscoveryService
import com.github.singularity.core.broadcast.JmdnsDeviceBroadcastService
import com.github.singularity.core.broadcast.JmdnsDeviceDiscoveryService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val BroadcastModule = module {
    singleOf(::JmdnsDeviceBroadcastService) bind DeviceBroadcastService::class
    singleOf(::JmdnsDeviceDiscoveryService) bind DeviceDiscoveryService::class
}
