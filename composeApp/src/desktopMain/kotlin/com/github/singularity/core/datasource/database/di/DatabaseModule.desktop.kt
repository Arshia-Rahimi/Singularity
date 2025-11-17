package com.github.singularity.core.datasource.database.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.github.singularity.core.database.SingularityDatabase
import net.harawata.appdirs.AppDirsFactory
import org.koin.core.module.Module
import java.io.File
import java.util.Properties

actual fun Module.platformDatabaseModule() {
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
}
