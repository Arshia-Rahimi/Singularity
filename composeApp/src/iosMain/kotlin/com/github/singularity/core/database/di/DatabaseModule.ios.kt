package com.github.singularity.core.database.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.DatabaseConfiguration
import com.github.singularity.core.database.SingularityDatabase
import org.koin.core.module.Module

actual fun Module.platformDatabaseModule() {
	single {
		NativeSqliteDriver(
			schema = SingularityDatabase.Schema,
			name = "singularity.db",
			onConfiguration = { config: DatabaseConfiguration ->
				config.copy(
					extendedConfig = DatabaseConfiguration.Extended(foreignKeyConstraints = true)
				)
			},
		)
	}
}
