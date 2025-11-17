package com.github.singularity.core.datasource.network.di

import com.github.singularity.core.datasource.memory.PairRequestDataSource
import com.github.singularity.core.datasource.memory.impl.InMemoryPairRequestDataSource
import com.github.singularity.core.datasource.network.SyncGroupServer
import com.github.singularity.core.datasource.network.impl.KtorSyncGroupServer
import com.github.singularity.core.shared.canHostSyncServer
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun Module.platformNetworkModule()

val NetworkDataSourceModule = module {
    platformNetworkModule()
    if (canHostSyncServer) {
        singleOf(::KtorSyncGroupServer) bind SyncGroupServer::class
        singleOf(::InMemoryPairRequestDataSource) bind PairRequestDataSource::class
    }
}
