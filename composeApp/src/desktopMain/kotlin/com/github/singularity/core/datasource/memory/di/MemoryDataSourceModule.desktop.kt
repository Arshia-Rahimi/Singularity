package com.github.singularity.core.datasource.memory.di

import com.github.singularity.core.datasource.memory.PairRequestDataSource
import com.github.singularity.core.datasource.memory.impl.InMemoryPairRequestDataSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.platformMemoryDataSourceModule() {
    singleOf(::InMemoryPairRequestDataSource) bind PairRequestDataSource::class
}
