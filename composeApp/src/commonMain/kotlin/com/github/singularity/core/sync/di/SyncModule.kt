package com.github.singularity.core.sync.di

import com.github.singularity.core.shared.canHostSyncServer
import com.github.singularity.core.sync.datasource.InMemoryPairRequestDataSource
import com.github.singularity.core.sync.datasource.KtorSyncGroupServer
import com.github.singularity.core.sync.datasource.KtorSyncRemoteDataSource
import com.github.singularity.core.sync.datasource.PairRequestDataSource
import com.github.singularity.core.sync.datasource.SyncEventBridge
import com.github.singularity.core.sync.datasource.SyncEventBridgeImpl
import com.github.singularity.core.sync.datasource.SyncGroupServer
import com.github.singularity.core.sync.datasource.SyncRemoteDataSource
import com.github.singularity.core.sync.plugin.Plugin
import com.github.singularity.core.sync.plugin.clipboard.ClipboardPlugin
import com.github.singularity.core.sync.service.ClientSyncService
import com.github.singularity.core.sync.service.ServerSyncService
import com.github.singularity.core.sync.service.SyncService
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val SyncModule = module {
    single<SyncService>(createdAtStart = true) {
        if (canHostSyncServer) ServerSyncService(get(), get(), get(), getKoin().getAll(), get())
        else ClientSyncService(get(), getKoin().getAll(), get())
    } bind SyncService::class

    singleOf(::SyncEventBridgeImpl) bind SyncEventBridge::class
    factoryOf(::KtorSyncRemoteDataSource) bind SyncRemoteDataSource::class
    if (canHostSyncServer) {
        singleOf(::KtorSyncGroupServer) bind SyncGroupServer::class
        singleOf(::InMemoryPairRequestDataSource) bind PairRequestDataSource::class
    }

    // plugins
    singleOf(::ClipboardPlugin) bind Plugin::class
}
