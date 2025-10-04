package com.github.singularity.ui.di

import com.github.singularity.ui.feature.broadcast.BroadcastViewModel
import com.github.singularity.ui.feature.discover.DiscoverViewModel
import com.github.singularity.ui.feature.log.LogViewModel
import com.github.singularity.ui.feature.settings.SettingsViewModel
import com.github.singularity.ui.navigation.NavigationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ViewmodelModule = module {
    viewModelOf(::NavigationViewModel)
    viewModelOf(::DiscoverViewModel)
    viewModelOf(::BroadcastViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::LogViewModel)
}
