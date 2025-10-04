package com.github.singularity.core.broadcast.di

import com.github.singularity.core.broadcast.DeviceBroadcastService
import com.github.singularity.core.broadcast.DeviceDiscoverService
import com.github.singularity.core.broadcast.JmdnsDeviceBroadcastService
import com.github.singularity.core.broadcast.JmdnsDeviceDiscoverService
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.broadcastService() =
    singleOf(::JmdnsDeviceBroadcastService) bind DeviceBroadcastService::class

actual fun Module.discoverService() =
    singleOf(::JmdnsDeviceDiscoverService) bind DeviceDiscoverService::class
