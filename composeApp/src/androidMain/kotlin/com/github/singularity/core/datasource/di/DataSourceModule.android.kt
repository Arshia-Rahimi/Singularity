package com.github.singularity.core.datasource.di

import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.datasource.DeviceDiscoveryService
import com.github.singularity.core.datasource.ResourceLoader
import com.github.singularity.core.datasource.impl.AndroidResourceLoaderImpl
import com.github.singularity.core.datasource.impl.DeviceDiscoveryServiceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.platformDataSourceModule() {
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = SingularityDatabase.Schema,
            context = androidContext(),
            name = "singularity.db",
            callback = object : AndroidSqliteDriver.Callback(SingularityDatabase.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.setForeignKeyConstraintsEnabled(true)
                }
            }
        )
    }
    singleOf(::DeviceDiscoveryServiceImpl) bind DeviceDiscoveryService::class
    factoryOf(::AndroidResourceLoaderImpl) bind ResourceLoader::class
}
