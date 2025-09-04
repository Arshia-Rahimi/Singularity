package com.github.singularity.core.data.di

import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.data.impl.BroadcastRepositoryImp
import com.github.singularity.core.mdns.canHostSyncServer
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ServerDataModule = module {
    if (canHostSyncServer) singleOf(::BroadcastRepositoryImp) bind BroadcastRepository::class
}
