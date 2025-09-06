package com.github.singularity.core.data.di

import com.github.singularity.core.data.ClientConnectionRepository
import com.github.singularity.core.data.DiscoverRepository
import com.github.singularity.core.data.PluginEventsRepository
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.data.impl.ClientConnectionRepositoryImpl
import com.github.singularity.core.data.impl.DiscoverRepositoryImp
import com.github.singularity.core.data.impl.OfflinePreferencesRepository
import com.github.singularity.core.data.impl.PluginEventsRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val DataModule = module {
    singleOf(::ClientConnectionRepositoryImpl) bind ClientConnectionRepository::class
    singleOf(::OfflinePreferencesRepository) bind PreferencesRepository::class
    singleOf(::DiscoverRepositoryImp) bind DiscoverRepository::class
    singleOf(::PluginEventsRepositoryImpl) bind PluginEventsRepository::class
}
