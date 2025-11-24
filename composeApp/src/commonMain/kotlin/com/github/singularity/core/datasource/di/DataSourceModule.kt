package com.github.singularity.core.datasource.di

import com.github.singularity.core.datasource.database.di.databaseDataSourceModule
import com.github.singularity.core.datasource.memory.di.memoryDataSourceModule
import com.github.singularity.core.datasource.network.di.networkDataSourceModule
import com.github.singularity.core.datasource.presence.di.presenceDataSourceModule
import org.koin.dsl.module

val DataSourceModule = module {
	databaseDataSourceModule()
	memoryDataSourceModule()
	presenceDataSourceModule()
	networkDataSourceModule()
}
