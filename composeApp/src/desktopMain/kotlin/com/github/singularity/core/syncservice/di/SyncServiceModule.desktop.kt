package com.github.singularity.core.syncservice.di

import com.github.singularity.core.syncservice.ServerSyncService
import com.github.singularity.core.syncservice.SyncService
import org.koin.core.module.Module
import org.koin.dsl.bind

actual fun Module.singleOfSyncService() =
	single(createdAtStart = true) {
		ServerSyncService(get(), get(), get(), get(), get())
	} bind SyncService::class

