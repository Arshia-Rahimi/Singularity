package com.github.singularity.core.syncservice.di

import com.github.singularity.core.syncservice.plugin.clipboard.PlatformClipboardPlugin
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module

actual fun Module.singleOfPlatformClipboardPlugin(): KoinDefinition<out PlatformClipboardPlugin> {
	TODO("Not yet implemented")
}