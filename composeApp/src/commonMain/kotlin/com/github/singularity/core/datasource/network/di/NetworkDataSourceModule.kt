package com.github.singularity.core.datasource.network.di

import com.github.singularity.core.datasource.network.SyncRemoteDataSource
import com.github.singularity.core.datasource.network.impl.KtorSyncRemoteDataSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

fun Module.networkDataSourceModule() {
	singleOf(::KtorSyncRemoteDataSource) bind SyncRemoteDataSource::class
}
