package com.github.singularity.core.database.di

import com.github.singularity.core.database.HostedSyncGroupsDataSource
import com.github.singularity.core.database.JoinedSyncGroupsDataSource
import com.github.singularity.core.database.PreferencesDataSource
import com.github.singularity.core.database.impl.SqliteHostedSyncGroupsDataSource
import com.github.singularity.core.database.impl.SqliteJoinedSyncGroupsDataSource
import com.github.singularity.core.database.impl.SqlitePreferencesDataSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun Module.driver()

val DatabaseModule = module {
    driver()
    factoryOf(::SqliteHostedSyncGroupsDataSource) bind HostedSyncGroupsDataSource::class
    factoryOf(::SqliteJoinedSyncGroupsDataSource) bind JoinedSyncGroupsDataSource::class
    factoryOf(::SqlitePreferencesDataSource) bind PreferencesDataSource::class
}
