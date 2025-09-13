package com.github.singularity.core.sync.di

import com.github.singularity.core.shared.canHostSyncServer
import com.github.singularity.core.sync.ServerSyncService
import com.github.singularity.core.sync.SyncService
import com.github.singularity.core.sync.plugin.PluginsList
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val SyncModule = module {
    PluginsList.forEach { plugin -> singleOf(plugin) }

    if (canHostSyncServer) singleOf(::ServerSyncService) bind SyncService::class
    else singleOf(::SyncService)
}
