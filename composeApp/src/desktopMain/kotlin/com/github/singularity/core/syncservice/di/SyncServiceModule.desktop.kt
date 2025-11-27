package com.github.singularity.core.syncservice.di

import com.github.singularity.core.syncservice.ServerSyncService
import com.github.singularity.core.syncservice.SyncService
import com.github.singularity.core.syncservice.plugin.clipboard.JvmClipboardPlugin
import com.github.singularity.core.syncservice.plugin.clipboard.PlatformClipboardPlugin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.singleOfPlatformClipboardPlugin() =
	singleOf(::JvmClipboardPlugin) bind PlatformClipboardPlugin::class

actual fun Module.singleOfSyncService() =
	single(createdAtStart = true) {
		ServerSyncService(get(), get(), get(), get(), get(), get())
	} bind SyncService::class
