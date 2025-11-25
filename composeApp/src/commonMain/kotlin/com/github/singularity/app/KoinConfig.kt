package com.github.singularity.app

import com.github.singularity.app.navigation.Navigator
import com.github.singularity.core.data.di.DataModule
import com.github.singularity.core.datasource.di.DataSourceModule
import com.github.singularity.core.log.di.LoggerModule
import com.github.singularity.core.syncservice.di.SyncServiceModule
import com.github.singularity.ui.di.ViewmodelModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.module

private val AppModule = module {
	viewModelOf(::MainViewModel)
	singleOf(::Navigator)
}

private val CommonModules = listOf(
	AppModule,
	ViewmodelModule,
	DataModule,
	SyncServiceModule,
	LoggerModule,
	DataSourceModule,
)

expect val PlatformModules: List<Module>

val KoinConfig = KoinConfiguration {
	modules(CommonModules + PlatformModules)
}
