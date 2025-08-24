package com.github.singularity.ui.di

import com.github.singularity.ui.feature.discover.DiscoverViewModel
import com.github.singularity.ui.feature.main.MainViewModel
import com.github.singularity.ui.feature.publish.PublishViewModel
import com.github.singularity.ui.feature.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ViewmodelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::DiscoverViewModel)
    viewModelOf(::PublishViewModel)
    viewModelOf(::SettingsViewModel)
}
