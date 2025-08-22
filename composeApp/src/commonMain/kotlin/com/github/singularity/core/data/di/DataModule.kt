package com.github.singularity.core.data.di

import com.github.singularity.core.data.DeviceDiscoveryRepository
import com.github.singularity.core.data.SettingsRepository
import com.github.singularity.core.data.impl.DataStoreSettingsRepository
import com.github.singularity.core.data.impl.MdnsDeviceDiscoveryRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val DataModule = module {
    factoryOf(::DataStoreSettingsRepository) { bind<SettingsRepository>() }
    factoryOf(::MdnsDeviceDiscoveryRepository) { bind<DeviceDiscoveryRepository>() }
}
