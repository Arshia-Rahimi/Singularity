package com.github.singularity.core.server.di

import com.github.singularity.core.server.KtorLocalServer
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val ServerModule = module {
    singleOf(::KtorLocalServer)
}
