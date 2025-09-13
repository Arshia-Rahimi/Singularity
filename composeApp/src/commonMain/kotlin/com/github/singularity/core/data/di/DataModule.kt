package com.github.singularity.core.data.di

import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.DiscoverRepository
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.PairRequestRepository
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.data.SyncEventRepository
import com.github.singularity.core.data.impl.BroadcastRepositoryImp
import com.github.singularity.core.data.impl.ClientConnectionRepositoryImpl
import com.github.singularity.core.data.impl.DiscoverRepositoryImp
import com.github.singularity.core.data.impl.HostedSyncGroupRepositoryImpl
import com.github.singularity.core.data.impl.PairRequestRepositoryImpl
import com.github.singularity.core.data.impl.SQLPreferencesRepository
import com.github.singularity.core.data.impl.ServerConnectionRepositoryImpl
import com.github.singularity.core.data.impl.SyncEventRepositoryImpl
import com.github.singularity.core.shared.canHostSyncServer
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val DataModule = module {
    singleOf(::ClientConnectionRepositoryImpl) bind ClientConnectionRepository::class
    singleOf(::SQLPreferencesRepository) bind PreferencesRepository::class
    singleOf(::DiscoverRepositoryImp) bind DiscoverRepository::class
    singleOf(::SyncEventRepositoryImpl) bind SyncEventRepository::class

    if (canHostSyncServer) {
        singleOf(::HostedSyncGroupRepositoryImpl) bind HostedSyncGroupRepository::class
        singleOf(::BroadcastRepositoryImp) bind BroadcastRepository::class
        singleOf(::ServerConnectionRepositoryImpl) bind ServerConnectionRepository::class
        singleOf(::PairRequestRepositoryImpl) bind PairRequestRepository::class
    }
}
