package com.github.singularity.core.broadcast.di

import com.github.singularity.core.broadcast.DeviceBroadcastService
import com.github.singularity.core.broadcast.DeviceDiscoveryService
import com.github.singularity.core.broadcast.JmdnsDeviceDiscoveryService
import com.github.singularity.core.broadcast.ZeroconfDeviceBroadcastService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.broadcastService() =
    singleOf(::ZeroconfDeviceBroadcastService) bind DeviceBroadcastService::class

actual fun Module.discoverService() =
    singleOf(::JmdnsDeviceDiscoveryService) bind DeviceDiscoveryService::class
