package com.github.singularity.core.client.di

import com.github.singularity.core.client.PairRemoteDataSource
import com.github.singularity.core.client.SyncEventRemoteDataSource
import com.github.singularity.core.client.impl.KtorPairRemoteDataSource
import com.github.singularity.core.client.impl.KtorSyncEventRemoteDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ClientModule = module {
    factoryOf(::KtorPairRemoteDataSource) bind PairRemoteDataSource::class
    factoryOf(::KtorSyncEventRemoteDataSource) bind SyncEventRemoteDataSource::class
}
