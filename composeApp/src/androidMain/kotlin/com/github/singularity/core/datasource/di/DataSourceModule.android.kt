package com.github.singularity.core.datasource.di

import com.github.singularity.core.datasource.presence.DeviceDiscoveryService
import com.github.singularity.core.datasource.presence.impl.DeviceDiscoveryServiceImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.platformDataSourceModule() {
    singleOf(::DeviceDiscoveryServiceImpl) bind DeviceDiscoveryService::class

}
