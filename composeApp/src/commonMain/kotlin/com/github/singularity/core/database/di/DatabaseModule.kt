package com.github.singularity.core.database.di

import com.github.singularity.core.database.HostedSyncGroupsLocalDataSource
import com.github.singularity.core.database.JoinedSyncGroupsLocalDataSource
import com.github.singularity.core.database.PreferencesLocalDataSource
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.database.impl.SqlDelightHostedSyncGroupsLocalDataSource
import com.github.singularity.core.database.impl.SqlDelightJoinedSyncGroupsLocalDataSource
import com.github.singularity.core.database.impl.SqlDelightPreferencesLocalDataSource
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun Module.database(): KoinDefinition<SingularityDatabase>

val DatabaseModule = module {
    database()
    factoryOf(::SqlDelightHostedSyncGroupsLocalDataSource) bind HostedSyncGroupsLocalDataSource::class
    factoryOf(::SqlDelightJoinedSyncGroupsLocalDataSource) bind JoinedSyncGroupsLocalDataSource::class
    factoryOf(::SqlDelightPreferencesLocalDataSource) bind PreferencesLocalDataSource::class
}
