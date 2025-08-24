package com.github.singularity.core.database.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.github.singularity.core.database.sql.SingularityDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val DatabaseModule = module {
    single {
        AndroidSqliteDriver(SingularityDatabase.Schema, androidContext(), "singularity.db")
    }
}
