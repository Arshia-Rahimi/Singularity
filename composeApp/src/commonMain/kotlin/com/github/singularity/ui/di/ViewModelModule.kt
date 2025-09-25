package com.github.singularity.ui.di

import com.github.singularity.ui.feature.main.MainViewModel
import com.github.singularity.ui.feature.settings.SettingsViewModel
import com.github.singularity.ui.navigation.NavigationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ViewmodelModule = module {
    viewModelOf(::NavigationViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::SettingsViewModel)
}
