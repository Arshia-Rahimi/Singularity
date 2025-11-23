package com.github.singularity.ui.di

import com.github.singularity.ui.feature.connection.client.ClientViewModel
import com.github.singularity.ui.feature.log.LogViewModel
import com.github.singularity.ui.feature.permissions.PermissionsViewModel
import com.github.singularity.ui.feature.plugins.PluginsViewModel
import com.github.singularity.ui.feature.settings.SettingsViewModel
import com.github.singularity.ui.feature.test.TestViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect fun Module.platformViewModelModule()

val ViewmodelModule = module {
    platformViewModelModule()

	//
    viewModelOf(::ClientViewModel)
	viewModelOf(::PluginsViewModel)
    viewModelOf(::PermissionsViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::LogViewModel)

    //
    viewModelOf(::TestViewModel)
}
