package com.github.singularity.core.datasource.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.github.singularity.core.database.SingularityDatabase
import com.github.singularity.core.datasource.DeviceBroadcastService
import com.github.singularity.core.datasource.DeviceDiscoveryService
import com.github.singularity.core.datasource.HostedSyncGroupsLocalDataSource
import com.github.singularity.core.datasource.PairRequestDataSource
import com.github.singularity.core.datasource.ResourceLoader
import com.github.singularity.core.datasource.impl.DesktopResourceLoader
import com.github.singularity.core.datasource.impl.InMemoryPairRequestDataSource
import com.github.singularity.core.datasource.impl.KtorSyncGroupServer
import com.github.singularity.core.datasource.impl.SqlDelightHostedSyncGroupsLocalDataSource
import com.github.singularity.core.datasource.impl.ZeroconfDeviceBroadcastService
import com.github.singularity.core.datasource.impl.ZeroconfDeviceDiscoveryService
import net.harawata.appdirs.AppDirsFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import java.io.File
import java.util.Properties

actual fun Module.platformDataSourceModule() {
    single<SqlDriver> {
        val directory = AppDirsFactory.getInstance()
            .getUserDataDir("Singularity", null, null)
        File(directory).mkdirs()

        val databaseFile = File(directory, "singularity.db")
        val driver = JdbcSqliteDriver(
            url = "jdbc:sqlite:${databaseFile.absolutePath}",
            schema = SingularityDatabase.Schema,
            properties = Properties().apply { put("foreign_keys", "true") },
        )

        if (!databaseFile.exists()) {
            SingularityDatabase.Schema.create(driver)
        }

        driver
    }
    singleOf(::SqlDelightHostedSyncGroupsLocalDataSource) bind HostedSyncGroupsLocalDataSource::class
    singleOf(::InMemoryPairRequestDataSource) bind PairRequestDataSource::class
    singleOf(::ZeroconfDeviceDiscoveryService) bind DeviceDiscoveryService::class
    singleOf(::ZeroconfDeviceBroadcastService) bind DeviceBroadcastService::class
    singleOf(::KtorSyncGroupServer) bind `SyncGroupServer.desktop`::class
    singleOf(::DesktopResourceLoader) bind ResourceLoader::class
}
