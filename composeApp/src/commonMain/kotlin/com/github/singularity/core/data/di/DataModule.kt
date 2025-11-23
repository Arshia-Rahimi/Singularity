package com.github.singularity.core.data.di

import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.DiscoverRepository
import com.github.singularity.core.data.JoinedSyncGroupRepository
import com.github.singularity.core.data.PluginSettingsRepository
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.data.impl.ClientConnectionRepositoryImpl
import com.github.singularity.core.data.impl.DiscoverRepositoryImpl
import com.github.singularity.core.data.impl.JoinedSyncGroupRepositoryImpl
import com.github.singularity.core.data.impl.SqlitePluginSettingsRepository
import com.github.singularity.core.data.impl.SqlitePreferencesRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun Module.platformDataModule()

val DataModule = module {
    platformDataModule()
    singleOf(::ClientConnectionRepositoryImpl) bind ClientConnectionRepository::class
    singleOf(::SqlitePreferencesRepository) bind PreferencesRepository::class
	singleOf(::SqlitePluginSettingsRepository) bind PluginSettingsRepository::class
    singleOf(::DiscoverRepositoryImpl) bind DiscoverRepository::class
    singleOf(::JoinedSyncGroupRepositoryImpl) bind JoinedSyncGroupRepository::class
}
