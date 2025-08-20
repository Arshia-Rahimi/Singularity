package com.github.singularity.app.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun initKoin(
    platformSpecificModules: List<Module> = emptyList(),
) {
    startKoin {
        modules(ModulesList + platformSpecificModules)
    }
}
