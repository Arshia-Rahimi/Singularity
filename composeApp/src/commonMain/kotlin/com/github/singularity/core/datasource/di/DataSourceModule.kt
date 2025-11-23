package com.github.singularity.core.datasource.di

import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.datasource.JoinedSyncGroupsLocalDataSource
import com.github.singularity.core.datasource.PluginSettingsDataSource
import com.github.singularity.core.datasource.PreferencesLocalDataSource
import com.github.singularity.core.datasource.SyncEventBridge
import com.github.singularity.core.datasource.SyncRemoteDataSource
import com.github.singularity.core.datasource.impl.KtorSyncRemoteDataSource
import com.github.singularity.core.datasource.impl.SqlDelightJoinedSyncGroupsLocalDataSource
import com.github.singularity.core.datasource.impl.SqlDelightPluginSettingsDataSource
import com.github.singularity.core.datasource.impl.SqlDelightPreferencesLocalDataSource
import com.github.singularity.core.datasource.impl.SyncEventBridgeImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun Module.platformDataSourceModule()

val DataSourceModule = module {
    platformDataSourceModule()
	single { SingularityDatabase(get()) }
    singleOf(::SqlDelightJoinedSyncGroupsLocalDataSource) bind JoinedSyncGroupsLocalDataSource::class
    singleOf(::SqlDelightPreferencesLocalDataSource) bind PreferencesLocalDataSource::class
    singleOf(::SqlDelightPluginSettingsDataSource) bind PluginSettingsDataSource::class
    singleOf(::SyncEventBridgeImpl) bind SyncEventBridge::class
    singleOf(::KtorSyncRemoteDataSource) bind SyncRemoteDataSource::class
}
