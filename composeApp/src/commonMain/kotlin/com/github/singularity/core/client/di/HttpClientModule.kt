package com.github.singularity.core.client.di

import com.github.singularity.core.client.HttpClientDataSource
import com.github.singularity.core.client.WebSocketClientDataSource
import com.github.singularity.core.client.impl.KtorHttpClientDataSource
import com.github.singularity.core.client.impl.KtorWebSocketClientDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ClientModule = module {
    singleOf(::KtorHttpClientDataSource) bind HttpClientDataSource::class
    singleOf(::KtorWebSocketClientDataSource) bind WebSocketClientDataSource::class
}
