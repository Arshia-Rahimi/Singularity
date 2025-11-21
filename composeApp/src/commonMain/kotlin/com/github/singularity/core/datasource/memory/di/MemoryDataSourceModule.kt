package com.github.singularity.core.datasource.memory.di

import com.github.singularity.core.datasource.memory.SyncEventBridge
import com.github.singularity.core.datasource.memory.impl.SyncEventBridgeImpl
import com.github.singularity.core.datasource.network.SyncRemoteDataSource
import com.github.singularity.core.datasource.network.impl.KtorSyncRemoteDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val MemoryDataSourceModule = module {
    singleOf(::SyncEventBridgeImpl) bind SyncEventBridge::class
	singleOf(::KtorSyncRemoteDataSource) bind SyncRemoteDataSource::class
}
