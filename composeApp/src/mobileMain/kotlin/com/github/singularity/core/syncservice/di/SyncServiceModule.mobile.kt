package com.github.singularity.core.syncservice.di

import com.github.singularity.core.syncservice.ClientSyncService
import com.github.singularity.core.syncservice.SyncService
import org.koin.core.module.Module
import org.koin.dsl.bind

actual fun Module.singleOfSyncServiceImpl() {
	single(createdAtStart = true) {
		ClientSyncService(get(), get(), getKoin().getAll())
	} bind SyncService::class
}
