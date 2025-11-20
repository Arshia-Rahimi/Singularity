package com.github.singularity.core.datasource.resource.di

import com.github.singularity.core.datasource.resource.ResourceLoader
import com.github.singularity.core.datasource.resource.impl.IosResourceLoader
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

actual fun Module.platformResourceModule() {
	factoryOf(::IosResourceLoader) bind ResourceLoader::class
}
