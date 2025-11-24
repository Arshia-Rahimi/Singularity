package com.github.singularity.core.datasource.memory.di

import com.github.singularity.core.datasource.memory.SyncEventBridge
import com.github.singularity.core.datasource.memory.impl.SyncEventBridgeImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

fun Module.memoryDataSourceModule() {
	singleOf(::SyncEventBridgeImpl) bind SyncEventBridge::class
}
