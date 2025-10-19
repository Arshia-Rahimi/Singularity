package com.github.singularity.ui.di

import com.github.singularity.ui.feature.home.client.ClientViewModel
import com.github.singularity.ui.feature.home.server.ServerViewModel
import com.github.singularity.ui.feature.log.LogViewModel
import com.github.singularity.ui.feature.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ViewmodelModule = module {
    viewModelOf(::ClientViewModel)
    viewModelOf(::ServerViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::LogViewModel)
}
