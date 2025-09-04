package com.github.singularity.core.data.di

import com.github.singularity.core.data.ConnectionRepository
import com.github.singularity.core.data.DiscoverRepository
import com.github.singularity.core.data.PreferencesRepository
import com.github.singularity.core.data.impl.ConnectionRepositoryImpl
import com.github.singularity.core.data.impl.DiscoverRepositoryImp
import com.github.singularity.core.data.impl.OfflinePreferencesRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val DataModule = module {
    singleOf(::ConnectionRepositoryImpl) bind ConnectionRepository::class
    factoryOf(::OfflinePreferencesRepository) bind PreferencesRepository::class
    factoryOf(::DiscoverRepositoryImp) bind DiscoverRepository::class
}
