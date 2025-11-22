package com.github.singularity.core.datasource.network.di

import com.github.singularity.core.datasource.network.SyncGroupServer
import com.github.singularity.core.datasource.network.impl.KtorSyncGroupServer
import com.github.singularity.core.datasource.network.presence.DeviceBroadcastService
import com.github.singularity.core.datasource.network.presence.DeviceDiscoveryService
import com.github.singularity.core.datasource.network.presence.ZeroconfDeviceBroadcastService
import com.github.singularity.core.datasource.network.presence.ZeroconfDeviceDiscoveryService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.platformNetworkModule() {
    singleOf(::ZeroconfDeviceDiscoveryService) bind DeviceDiscoveryService::class
    singleOf(::ZeroconfDeviceBroadcastService) bind DeviceBroadcastService::class
    singleOf(::KtorSyncGroupServer) bind SyncGroupServer::class
}
