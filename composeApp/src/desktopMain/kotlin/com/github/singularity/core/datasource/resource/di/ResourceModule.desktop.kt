package com.github.singularity.core.datasource.resource.di

import com.github.singularity.core.datasource.resource.ResourceLoader
import com.github.singularity.core.datasource.resource.impl.DesktopResourceLoader
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

actual fun Module.platformResourceModule() {
	factoryOf(::DesktopResourceLoader) bind ResourceLoader::class
}
