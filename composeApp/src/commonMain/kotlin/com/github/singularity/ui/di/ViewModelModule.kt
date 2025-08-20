package com.github.singularity.ui.di

import com.github.singularity.ui.feature.main.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ViewmodelModule = module {
    viewModelOf(::MainViewModel)
}
