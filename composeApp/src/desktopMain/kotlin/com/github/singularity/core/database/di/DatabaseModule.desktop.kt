package com.github.singularity.core.database.di

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.github.singularity.core.database.SingularityDatabase
import org.koin.core.module.Module
import java.util.Properties

actual fun Module.driver() {
    single {
        SingularityDatabase(
            JdbcSqliteDriver(
                url = "jdbc:sqlite:singularity.db",
                schema = SingularityDatabase.Schema,
                properties = Properties().apply { put("foreign_keys", "true") },
            )
        )
    }
}
