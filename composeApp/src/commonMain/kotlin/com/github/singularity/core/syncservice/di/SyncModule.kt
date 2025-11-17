package com.github.singularity.core.syncservice.di

import com.github.singularity.core.shared.canHostSyncServer
import com.github.singularity.core.syncservice.SyncService
import com.github.singularity.core.syncservice.impl.ClientSyncService
import com.github.singularity.core.syncservice.impl.ServerSyncService
import com.github.singularity.core.syncservice.plugin.Plugin
import com.github.singularity.core.syncservice.plugin.clipboard.ClipboardPlugin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val SyncModule = module {
    single<SyncService>(createdAtStart = true) {
        if (canHostSyncServer) ServerSyncService(get(), get(), get(), getKoin().getAll(), get())
        else ClientSyncService(get(), getKoin().getAll(), get())
    } bind SyncService::class

    // plugins
    singleOf(::ClipboardPlugin) bind Plugin::class
}
