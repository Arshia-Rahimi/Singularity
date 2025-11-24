package com.github.singularity.core.datasource.di

import com.github.singularity.core.datasource.database.HostedSyncGroupsLocalDataSource
import com.github.singularity.core.datasource.database.impl.SqlDelightHostedSyncGroupsLocalDataSource
import com.github.singularity.core.datasource.memory.PairRequestDataSource
import com.github.singularity.core.datasource.memory.impl.InMemoryPairRequestDataSource
import com.github.singularity.core.datasource.network.SyncGroupServer
import com.github.singularity.core.datasource.network.impl.KtorSyncGroupServer
import com.github.singularity.core.datasource.presence.DeviceBroadcastService
import com.github.singularity.core.datasource.presence.impl.ZeroconfDeviceBroadcastService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val DesktopDataSourceModule = module {
	singleOf(::SqlDelightHostedSyncGroupsLocalDataSource) bind HostedSyncGroupsLocalDataSource::class
	singleOf(::ZeroconfDeviceBroadcastService) bind DeviceBroadcastService::class
	singleOf(::InMemoryPairRequestDataSource) bind PairRequestDataSource::class
	singleOf(::KtorSyncGroupServer) bind SyncGroupServer::class
}
