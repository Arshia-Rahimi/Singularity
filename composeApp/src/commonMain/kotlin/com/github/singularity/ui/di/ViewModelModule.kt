package com.github.singularity.ui.di

import com.github.singularity.ui.feature.connection.ConnectionViewModel
import com.github.singularity.ui.feature.connection.client.ClientViewModel
import com.github.singularity.ui.feature.connection.server.ServerViewModel
import com.github.singularity.ui.feature.log.LogViewModel
import com.github.singularity.ui.feature.permissions.PermissionsViewModel
import com.github.singularity.ui.feature.settings.SettingsViewModel
import com.github.singularity.ui.feature.test.TestViewModel
import com.github.singularity.ui.navigation.NavigationViewmodel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ViewmodelModule = module {
	singleOf(::NavigationViewmodel)
	//
    viewModelOf(::ConnectionViewModel)
    viewModelOf(::ClientViewModel)
    viewModelOf(::ServerViewModel)
    viewModelOf(::PermissionsViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::LogViewModel)

    //
    viewModelOf(::TestViewModel)
}
