package com.github.singularity.core.service.di

import com.github.singularity.core.service.ClientConnectionService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val ClientServiceModule = module {
    singleOf(::ClientConnectionService)
}
