package com.github.singularity.core.datasource.di

import com.github.singularity.core.datasource.database.di.databaseDataSourceModule
import com.github.singularity.core.datasource.network.di.networkDataSourceModule
import org.koin.dsl.module

val DataSourceModule = module {
	databaseDataSourceModule()
	networkDataSourceModule()
}
