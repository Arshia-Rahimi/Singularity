package com.github.singularity.core.datasource.database.di

import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.github.singularity.core.database.SingularityDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module

actual fun Module.singleOfSqlDriver(): KoinDefinition<out SqlDriver> =
	single {
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

