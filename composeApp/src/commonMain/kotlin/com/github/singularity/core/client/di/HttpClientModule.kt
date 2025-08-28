package com.github.singularity.core.client.di

import com.github.singularity.core.client.HttpClientDataSource
import com.github.singularity.core.client.http.KtorHttpClient
import com.github.singularity.core.client.http.KtorHttpClientDataSource
import com.github.singularity.core.client.websocket.KtorWebSocketClient
import com.github.singularity.core.client.websocket.WebSocketClientDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ClientModule = module {
    factoryOf(::KtorHttpClient)
    factoryOf(::KtorWebSocketClient)
    factoryOf(::KtorHttpClientDataSource) bind HttpClientDataSource::class
    factoryOf(::KtorWebSocketClient) bind WebSocketClientDataSource::class
}
