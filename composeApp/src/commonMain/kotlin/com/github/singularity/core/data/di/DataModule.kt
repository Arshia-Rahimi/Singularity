package com.github.singularity.core.data.di

import com.github.singularity.core.data.AuthTokenRepository
import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.DiscoverRepository
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.JoinedSyncGroupRepository
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.data.SyncEventBridge
import com.github.singularity.core.data.impl.BroadcastRepositoryImp
import com.github.singularity.core.data.impl.ClientConnectionRepositoryImpl
import com.github.singularity.core.data.impl.DiscoverRepositoryImp
import com.github.singularity.core.data.impl.HostedSyncGroupRepositoryImpl
import com.github.singularity.core.data.impl.JoinedSyncGroupRepositoryImpl
import com.github.singularity.core.data.impl.RandomTokenAuthRepository
import com.github.singularity.core.data.impl.ServerConnectionRepositoryImpl
import com.github.singularity.core.data.impl.SqlitePreferencesRepository
import com.github.singularity.core.data.impl.SyncEventBridgeImpl
import com.github.singularity.core.server.InMemoryPairRequestDataSourceImpl
import com.github.singularity.core.server.PairRequestDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val DataModule = module {
    singleOf(::ClientConnectionRepositoryImpl) bind ClientConnectionRepository::class
    singleOf(::SqlitePreferencesRepository) bind PreferencesRepository::class
    singleOf(::DiscoverRepositoryImp) bind DiscoverRepository::class
    singleOf(::SyncEventBridgeImpl) bind SyncEventBridge::class
    singleOf(::HostedSyncGroupRepositoryImpl) bind HostedSyncGroupRepository::class
    singleOf(::JoinedSyncGroupRepositoryImpl) bind JoinedSyncGroupRepository::class
    singleOf(::BroadcastRepositoryImp) bind BroadcastRepository::class
    singleOf(::ServerConnectionRepositoryImpl) bind ServerConnectionRepository::class
	singleOf(::InMemoryPairRequestDataSourceImpl) bind PairRequestDataSource::class
    singleOf(::RandomTokenAuthRepository) bind AuthTokenRepository::class
}
