package com.github.singularity.core.datasource.presence.di

import com.github.singularity.core.datasource.presence.DeviceDiscoveryService
import com.github.singularity.core.datasource.presence.impl.ZeroconfDeviceDiscoveryService
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.singleOfDeviceDiscoveryService(): KoinDefinition<out DeviceDiscoveryService> =
	singleOf(::ZeroconfDeviceDiscoveryService) bind DeviceDiscoveryService::class
