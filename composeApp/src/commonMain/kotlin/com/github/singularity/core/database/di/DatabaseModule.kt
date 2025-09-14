package com.github.singularity.core.database.di

import com.github.singularity.core.database.SqliteHostedSyncGroupsDataSource
import com.github.singularity.core.database.SqliteJoinedSyncGroupsDataSource
import com.github.singularity.core.database.SqlitePreferencesDataSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

expect fun Module.driver()

val DatabaseModule = module {
    driver()
    factoryOf(::SqliteHostedSyncGroupsDataSource)
    factoryOf(::SqliteJoinedSyncGroupsDataSource)
    factoryOf(::SqlitePreferencesDataSource)
}
