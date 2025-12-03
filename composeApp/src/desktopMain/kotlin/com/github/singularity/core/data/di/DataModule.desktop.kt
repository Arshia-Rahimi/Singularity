package com.github.singularity.core.data.di

import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.data.HostedSyncGroupRepository
import com.github.singularity.core.data.ServerConnectionRepository
import com.github.singularity.core.data.impl.BroadcastRepositoryImpl
import com.github.singularity.core.data.impl.HostedSyncGroupRepositoryImpl
import com.github.singularity.core.data.impl.ServerConnectionRepositoryImpl
import com.github.singularity.core.datasource.network.AuthTokenDataSource
import com.github.singularity.core.datasource.network.impl.RandomAuthTokenDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val DesktopDataModule = module {
    singleOf(::ServerConnectionRepositoryImpl) bind ServerConnectionRepository::class
    singleOf(::HostedSyncGroupRepositoryImpl) bind HostedSyncGroupRepository::class
    singleOf(::BroadcastRepositoryImpl) bind BroadcastRepository::class
	singleOf(::RandomAuthTokenDataSource) bind AuthTokenDataSource::class
}
