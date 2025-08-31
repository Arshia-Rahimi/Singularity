package com.github.singularity.core.client.di

import com.github.singularity.core.client.HttpClientDataSource
import com.github.singularity.core.client.WebSocketClientDataSource
import com.github.singularity.core.client.impl.KtorHttpClientDataSource
import com.github.singularity.core.client.impl.KtorWebSocketClientDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ClientModule = module {
    factoryOf(::KtorHttpClientDataSource) bind HttpClientDataSource::class
    factoryOf(::KtorWebSocketClientDataSource) bind WebSocketClientDataSource::class
}
