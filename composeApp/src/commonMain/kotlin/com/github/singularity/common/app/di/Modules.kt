package com.github.singularity.common.app.di

import com.github.singularity.common.ui.di.viewmodelModule
import org.koin.core.module.Module

val modulesList: List<Module> = listOf(
    viewmodelModule,
)
