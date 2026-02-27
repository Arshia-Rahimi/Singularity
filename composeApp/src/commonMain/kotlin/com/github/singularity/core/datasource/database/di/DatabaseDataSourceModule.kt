package com.github.singularity.core.datasource.database.di

import app.cash.sqldelight.db.SqlDriver
import com.github.singularity.core.database.Plugins
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.datasource.database.JoinedSyncGroupsLocalDataSource
import com.github.singularity.core.datasource.database.PluginSettingsDataSource
import com.github.singularity.core.datasource.database.PreferencesLocalDataSource
import com.github.singularity.core.datasource.database.impl.SqlDelightJoinedSyncGroupsLocalDataSource
import com.github.singularity.core.datasource.database.impl.SqlDelightPluginSettingsDataSource
import com.github.singularity.core.datasource.database.impl.SqlDelightPreferencesLocalDataSource
import com.github.singularity.core.datasource.database.impl.pluginSettingsAdapter
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

expect fun Module.singleOfSqlDriver(): KoinDefinition<out SqlDriver>

fun Module.databaseDataSourceModule() {
    singleOfSqlDriver()
    single {
        SingularityDatabase(
            driver = get(),
            PluginsAdapter = Plugins.Adapter(pluginSettingsAdapter),
        )
    }
    singleOf(::SqlDelightJoinedSyncGroupsLocalDataSource) bind JoinedSyncGroupsLocalDataSource::class
    singleOf(::SqlDelightPreferencesLocalDataSource) bind PreferencesLocalDataSource::class
    singleOf(::SqlDelightPluginSettingsDataSource) bind PluginSettingsDataSource::class
}
