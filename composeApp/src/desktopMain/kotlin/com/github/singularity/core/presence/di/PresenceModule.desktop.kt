package com.github.singularity.core.presence.di

import com.github.singularity.core.presence.DeviceBroadcastService
import com.github.singularity.core.presence.DeviceDiscoveryService
import com.github.singularity.core.presence.ZeroconfDeviceBroadcastService
import com.github.singularity.core.presence.ZeroconfDeviceDiscoveryService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.platformPresenceModule() {
    singleOf(::ZeroconfDeviceDiscoveryService) bind DeviceDiscoveryService::class
    singleOf(::ZeroconfDeviceBroadcastService) bind DeviceBroadcastService::class
}
