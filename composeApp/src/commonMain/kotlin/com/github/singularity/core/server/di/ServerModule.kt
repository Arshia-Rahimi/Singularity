package com.github.singularity.core.server.di

import com.github.singularity.authentication.AuthRepository
import com.github.singularity.core.server.KtorLocalServer
import com.github.singularity.core.server.LocalServerAuthRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ServerModule = module {
    factoryOf(::LocalServerAuthRepository) bind AuthRepository::class
    factoryOf(::KtorLocalServer)
}
