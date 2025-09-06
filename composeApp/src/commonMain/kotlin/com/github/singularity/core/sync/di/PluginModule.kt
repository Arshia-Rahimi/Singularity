package com.github.singularity.core.sync.di

import com.github.singularity.core.sync.plugins.PluginsList
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val PluginModule = module {
    PluginsList.forEach { plugin -> singleOf(plugin) }
}
