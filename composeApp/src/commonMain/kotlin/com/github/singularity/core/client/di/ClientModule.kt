package com.github.singularity.core.client.di

import com.github.singularity.core.client.SyncRemoteDataSource
import com.github.singularity.core.client.impl.KtorSyncRemoteDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ClientModule = module {
    factoryOf(::KtorSyncRemoteDataSource) bind SyncRemoteDataSource::class
}
