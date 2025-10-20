package com.github.singularity.core.broadcast.di

import com.github.singularity.core.broadcast.DeviceBroadcastService
import com.github.singularity.core.broadcast.DeviceDiscoveryService
import com.github.singularity.core.broadcast.ZeroconfDeviceBroadcastService
import com.github.singularity.core.broadcast.ZeroconfDeviceDiscoveryService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.broadcastService() =
    singleOf(::ZeroconfDeviceBroadcastService) bind DeviceBroadcastService::class

actual fun Module.discoverService() =
    singleOf(::ZeroconfDeviceDiscoveryService) bind DeviceDiscoveryService::class
