package com.github.singularity.core.sync.di

import com.github.singularity.core.shared.canHostSyncServer
import com.github.singularity.core.sync.ClientSyncService
import com.github.singularity.core.sync.ServerSyncService
import com.github.singularity.core.sync.SyncService
import com.github.singularity.core.sync.plugin.Plugin
import com.github.singularity.core.sync.plugin.clipboard.ClipboardPlugin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val SyncModule = module {
    single<SyncService>(createdAtStart = true) {
        if (canHostSyncServer) ServerSyncService(get(), get(), get(), getKoin().getAll(), get())
        else ClientSyncService(get(), getKoin().getAll(), get())
    } bind SyncService::class

    singleOf(::ClipboardPlugin) bind Plugin::class
}
