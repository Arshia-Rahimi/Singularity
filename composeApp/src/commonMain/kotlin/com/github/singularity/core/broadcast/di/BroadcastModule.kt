package com.github.singularity.core.broadcast.di

import com.github.singularity.core.broadcast.DeviceBroadcastService
import com.github.singularity.core.broadcast.DeviceDiscoveryService
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun Module.broadcastService(): KoinDefinition<out DeviceBroadcastService>

expect fun Module.discoverService(): KoinDefinition<out DeviceDiscoveryService>

val BroadcastModule = module {
    broadcastService()
    discoverService()
}
