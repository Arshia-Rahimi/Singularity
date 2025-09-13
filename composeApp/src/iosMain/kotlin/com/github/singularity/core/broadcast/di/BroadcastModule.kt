package com.github.singularity.core.broadcast.di

import com.github.singularity.core.broadcast.DeviceBroadcastService
import com.github.singularity.core.broadcast.DeviceBroadcastServiceImpl
import com.github.singularity.core.broadcast.DeviceDiscoveryService
import com.github.singularity.core.broadcast.DeviceDiscoveryServiceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val BroadcastModule = module {
    singleOf(::DeviceBroadcastServiceImpl) bind DeviceBroadcastService::class
    singleOf(::DeviceDiscoveryServiceImpl) bind DeviceDiscoveryService::class
}
