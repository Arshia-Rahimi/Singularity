package com.github.singularity.di

import com.github.singularity.authentication.AuthRepository
import com.github.singularity.authentication.ServerAuthRepository
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.registerDi() {
    install(Koin) {
        module {
            factoryOf(::ServerAuthRepository) { bind<AuthRepository>() }
        }
    }
}
