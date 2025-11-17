package com.github.singularity.core.datasource.network.di

import com.github.singularity.core.datasource.network.presence.DeviceBroadcastService
import com.github.singularity.core.datasource.network.presence.DeviceBroadcastServiceImpl
import com.github.singularity.core.datasource.network.presence.DeviceDiscoveryService
import com.github.singularity.core.datasource.network.presence.DeviceDiscoveryServiceImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.platformNetworkModule() {
    singleOf(::DeviceDiscoveryServiceImpl) bind DeviceDiscoveryService::class
    singleOf(::DeviceBroadcastServiceImpl) bind DeviceBroadcastService::class
}
