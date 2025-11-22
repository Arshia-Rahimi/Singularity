package com.github.singularity.core.syncservice.di

import com.github.singularity.core.syncservice.SyncService
import com.github.singularity.core.syncservice.impl.ServerSyncService
import org.koin.core.module.Module
import org.koin.dsl.bind

actual fun Module.platformSyncServiceModule() {
    single(createdAtStart = true) {
        ServerSyncService(get(), get(), get(), getKoin().getAll(), get())
    } bind SyncService::class
}
