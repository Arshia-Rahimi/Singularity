package com.github.singularity.core.datasource.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.DatabaseConfiguration
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.datasource.DeviceDiscoveryService
import com.github.singularity.core.datasource.ResourceLoader
import com.github.singularity.core.datasource.impl.DeviceDiscoveryServiceImpl
import com.github.singularity.core.datasource.impl.IosResourceLoader
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.platformDataSourceModule() {
    single<SqlDriver> {
        NativeSqliteDriver(
            schema = SingularityDatabase.Schema,
            name = "singularity.db",
            onConfiguration = { config: DatabaseConfiguration ->
                config.copy(
                    extendedConfig = DatabaseConfiguration.Extended(foreignKeyConstraints = true)
                )
            },
        )
    }
    singleOf(::DeviceDiscoveryServiceImpl) bind DeviceDiscoveryService::class
    singleOf(::IosResourceLoader) bind ResourceLoader::class
}
