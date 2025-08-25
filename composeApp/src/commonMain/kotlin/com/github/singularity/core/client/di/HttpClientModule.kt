package com.github.singularity.core.client.di

import com.github.singularity.core.client.KtorHttpClient
import com.github.singularity.core.client.KtorWebsocketClient
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val ClientModule = module {
    factoryOf(::KtorHttpClient)
    factoryOf(::KtorWebsocketClient)
}
