package com.github.singularity.core.discover.di

import com.github.singularity.core.discover.DeviceBroadcastService
import com.github.singularity.core.discover.DeviceDiscoveryService
import com.github.singularity.core.discover.impl.MdnsDeviceBroadcastService
import com.github.singularity.core.discover.impl.MdnsDeviceDiscoveryService
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val MdnsModule = module {
    factoryOf(::MdnsDeviceDiscoveryService) { bind<DeviceDiscoveryService>() }
    factoryOf(::MdnsDeviceBroadcastService) { bind<DeviceBroadcastService>() }
}
