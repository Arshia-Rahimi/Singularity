package com.github.singularity.core.server.di

import com.github.singularity.core.server.KtorHttpServer
import com.github.singularity.core.server.KtorWebSocketServer
import com.github.singularity.core.server.auth.AuthTokenRepository
import com.github.singularity.core.server.auth.JwtAuthTokenRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ServerModule = module {
    singleOf(::KtorHttpServer)
    singleOf(::KtorWebSocketServer)
    singleOf(::JwtAuthTokenRepository) bind AuthTokenRepository::class
}
