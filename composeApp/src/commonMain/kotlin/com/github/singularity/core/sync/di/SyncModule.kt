package com.github.singularity.core.sync.di

import com.github.singularity.core.shared.canHostSyncServer
import com.github.singularity.core.sync.ClientSyncService
import com.github.singularity.core.sync.ServerSyncService
import com.github.singularity.core.sync.SyncService
import org.koin.dsl.bind
import org.koin.dsl.module

val SyncModule = module {
    single<SyncService>(createdAtStart = true) {
        if (canHostSyncServer) ServerSyncService(get(), get(), get(), get(), get())
        else ClientSyncService(get(), get(), get())
    } bind SyncService::class
}
