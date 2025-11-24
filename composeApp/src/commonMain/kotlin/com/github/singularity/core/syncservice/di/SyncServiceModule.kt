package com.github.singularity.core.syncservice.di

import com.github.singularity.core.syncservice.plugin.Plugin
import com.github.singularity.core.syncservice.plugin.clipboard.ClipboardPlugin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun Module.singleOfSyncServiceImpl()

val SyncServiceModule = module {

	singleOfSyncServiceImpl()

    // plugins
    singleOf(::ClipboardPlugin) bind Plugin::class

}
