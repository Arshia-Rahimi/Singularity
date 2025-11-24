package com.github.singularity.core.datasource.presence.di

import com.github.singularity.core.datasource.presence.DeviceDiscoveryService
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module

expect fun Module.singleOfDeviceDiscoveryService(): KoinDefinition<out DeviceDiscoveryService>

fun Module.presenceDataSourceModule() {
	singleOfDeviceDiscoveryService()
}
