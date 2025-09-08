package com.github.singularity.core.service.di

import com.github.singularity.core.service.ClientServerConnectionService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val ServerServiceModule = module {
    singleOf(::ClientServerConnectionService)
}
