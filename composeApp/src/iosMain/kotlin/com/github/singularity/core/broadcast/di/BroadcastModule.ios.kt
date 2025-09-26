package com.github.singularity.core.broadcast.di

import com.github.singularity.core.broadcast.DeviceBroadcastService
import com.github.singularity.core.broadcast.DeviceBroadcastServiceImpl
import com.github.singularity.core.broadcast.DeviceDiscoverService
import com.github.singularity.core.broadcast.DeviceDiscoverServiceImpl
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.broadcastService(): KoinDefinition<out DeviceBroadcastService> =
    singleOf(::DeviceBroadcastServiceImpl) bind DeviceBroadcastService::class

actual fun Module.discoverService(): KoinDefinition<out DeviceDiscoverService> =
    singleOf(::DeviceDiscoverServiceImpl) bind DeviceDiscoverService::class
