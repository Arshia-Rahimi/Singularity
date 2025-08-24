package com.github.singularity.core.database.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.github.singularity.core.database.SingularityDatabase
import org.koin.core.module.Module

actual fun Module.driver() {
    single {
        SingularityDatabase(NativeSqliteDriver(SingularityDatabase.Schema, "singularity.db"))
    }
}
