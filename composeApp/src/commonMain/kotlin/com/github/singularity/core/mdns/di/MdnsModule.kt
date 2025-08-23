package com.github.singularity.core.mdns.di

import com.github.singularity.core.mdns.DeviceDiscoveryService
import com.github.singularity.core.mdns.MdnsDeviceDiscoveryService
import com.github.singularity.core.mdns.canHostSyncServer
import org.koin.dsl.bind
import org.koin.dsl.module

val MdnsModule = module {
    factory {
        MdnsDeviceDiscoveryService(
            shouldBroadcastDevice = canHostSyncServer,
            scope = get(),
            preferencesRepo = get(),
        )
    }.bind<DeviceDiscoveryService>()
}
