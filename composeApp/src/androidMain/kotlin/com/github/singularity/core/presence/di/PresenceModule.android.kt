package com.github.singularity.core.presence.di

import com.github.singularity.core.presence.DeviceBroadcastService
import com.github.singularity.core.presence.DeviceBroadcastServiceImpl
import com.github.singularity.core.presence.DeviceDiscoveryService
import com.github.singularity.core.presence.DeviceDiscoveryServiceImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.platformPresenceModule() {
    singleOf(::DeviceBroadcastServiceImpl) bind DeviceBroadcastService::class
    singleOf(::DeviceDiscoveryServiceImpl) bind DeviceDiscoveryService::class
}
