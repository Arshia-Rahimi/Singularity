package com.github.singularity.core.server.di

import com.github.singularity.core.server.KtorServer
import com.github.singularity.core.server.PairRequestDataSource
import com.github.singularity.core.server.impl.InMemoryPairRequestDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ServerModule = module {
	factoryOf(::KtorServer)
	singleOf(::InMemoryPairRequestDataSource) bind PairRequestDataSource::class
}
