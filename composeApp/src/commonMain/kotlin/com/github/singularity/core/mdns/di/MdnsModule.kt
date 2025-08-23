package com.github.singularity.core.mdns.di

import com.github.singularity.core.mdns.DeviceDiscoveryService
import com.github.singularity.core.mdns.MdnsDeviceDiscoveryService
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val MdnsModule = module {
    factoryOf(::MdnsDeviceDiscoveryService) { bind<DeviceDiscoveryService>() }
}
