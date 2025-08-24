package com.github.singularity.core.database.di

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.github.singularity.core.database.sql.SingularityDatabase
import org.koin.dsl.module
import java.util.Properties

actual val DatabaseModule = module {
    single {
        JdbcSqliteDriver("jdbc:sqlite:singularity.db", Properties(), SingularityDatabase.Schema)
    }
}
    
