package com.github.singularity.core.broadcast.di

import com.github.singularity.core.broadcast.DeviceBroadcastService
import com.github.singularity.core.broadcast.DeviceBroadcastServiceImpl
import com.github.singularity.core.broadcast.DeviceDiscoveryService
import com.github.singularity.core.broadcast.DeviceDiscoveryServiceImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.broadcastService() =
    singleOf(::DeviceBroadcastServiceImpl) bind DeviceBroadcastService::class

actual fun Module.discoverService() =
    singleOf(::DeviceDiscoveryServiceImpl) bind DeviceDiscoveryService::class
