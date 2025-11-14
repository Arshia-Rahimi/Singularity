package com.github.singularity.core.server.di

import com.github.singularity.core.server.PairRequestDataSource
import com.github.singularity.core.server.SyncGroupServer
import com.github.singularity.core.server.impl.InMemoryPairRequestDataSource
import com.github.singularity.core.server.impl.KtorSyncGroupServer
import com.github.singularity.core.shared.canHostSyncServer
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ServerModule = module {
	if (canHostSyncServer) {
		singleOf(::KtorSyncGroupServer) bind SyncGroupServer::class
		singleOf(::InMemoryPairRequestDataSource) bind PairRequestDataSource::class
	}
}
