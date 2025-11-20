package com.github.singularity.core.datasource.resource.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect fun Module.platformResourceModule()

val ResourceModule = module {
	platformResourceModule()
}
