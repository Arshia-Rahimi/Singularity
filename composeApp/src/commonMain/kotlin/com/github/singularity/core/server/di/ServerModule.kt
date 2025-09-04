package com.github.singularity.core.server.di

import com.github.singularity.authentication.AuthRepository
import com.github.singularity.core.server.KtorLocalServer
import com.github.singularity.core.server.LocalServerAuthRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ServerModule = module {
    singleOf(::LocalServerAuthRepository) bind AuthRepository::class
    singleOf(::KtorLocalServer)
}
