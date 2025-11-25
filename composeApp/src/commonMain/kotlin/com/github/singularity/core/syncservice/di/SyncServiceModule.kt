package com.github.singularity.core.syncservice.di

import com.github.singularity.core.syncservice.SyncService
import com.github.singularity.core.syncservice.plugin.Plugin
import com.github.singularity.core.syncservice.plugin.PluginWrapper
import com.github.singularity.core.syncservice.plugin.clipboard.ClipboardPlugin
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun Module.singleOfSyncService(): KoinDefinition<out SyncService>

val SyncServiceModule = module {
	single { PluginWrapper(getKoin().getAll()) }
    singleOf(::ClipboardPlugin) bind Plugin::class

	singleOfSyncService()

}
