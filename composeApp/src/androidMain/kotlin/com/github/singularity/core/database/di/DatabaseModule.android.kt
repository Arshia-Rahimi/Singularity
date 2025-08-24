package com.github.singularity.core.database.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.github.singularity.core.database.SingularityDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module

actual fun Module.driver() {
    single {
        SingularityDatabase(
            AndroidSqliteDriver(
                SingularityDatabase.Schema,
                androidContext(),
                "singularity.db"
            )
        )
    }
}
