package com.github.singularity.core.database.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.github.singularity.core.database.sql.SingularityDatabase
import org.koin.dsl.module

actual val DatabaseModule = module {
    single {
        NativeSqliteDriver(SingularityDatabase.Schema, "singularity.db")
    }
}
