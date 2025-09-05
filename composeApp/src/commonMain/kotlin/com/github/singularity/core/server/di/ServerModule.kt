package com.github.singularity.core.server.di

import com.github.singularity.core.server.KtorLocalServer
import com.github.singularity.core.server.crypto.AuthTokenRepository
import com.github.singularity.core.server.crypto.JwtAuthTokenRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ServerModule = module {
    singleOf(::KtorLocalServer)
    singleOf(::JwtAuthTokenRepository) bind AuthTokenRepository::class
}
