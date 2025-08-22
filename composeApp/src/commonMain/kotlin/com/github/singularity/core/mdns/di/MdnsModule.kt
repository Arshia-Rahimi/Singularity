package com.github.singularity.core.mdns.di

import com.github.singularity.core.mdns.MdnsDeviceDiscoveryService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val MdnsModule = module {
    singleOf(::MdnsDeviceDiscoveryService)
}
