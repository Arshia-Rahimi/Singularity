package com.github.singularity.core.syncservice.di

import com.github.singularity.core.syncservice.plugin.clipboard.AndroidClipboardPlugin
import com.github.singularity.core.syncservice.plugin.clipboard.PlatformClipboardPlugin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.singleOfPlatformClipboardPlugin() =
	singleOf(::AndroidClipboardPlugin) bind PlatformClipboardPlugin::class
