package com.github.singularity.core.datasource.presence.di

import com.github.singularity.core.datasource.presence.DeviceDiscoveryService
import com.github.singularity.core.datasource.presence.impl.DnsSdDeviceDiscoveryService
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.singleOfDeviceDiscoveryService(): KoinDefinition<out DeviceDiscoveryService> =
	singleOf(::DnsSdDeviceDiscoveryService) bind DeviceDiscoveryService::class
