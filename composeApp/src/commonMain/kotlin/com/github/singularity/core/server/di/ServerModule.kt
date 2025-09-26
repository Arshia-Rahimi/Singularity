package com.github.singularity.core.server.di

import com.github.singularity.core.server.KtorHttpServer
import com.github.singularity.core.server.KtorWebSocketServer
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val ServerModule = module {
    factoryOf(::KtorHttpServer)
    factoryOf(::KtorWebSocketServer)
}
