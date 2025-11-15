package com.github.singularity.core.broadcast.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect fun Module.platformBroadcastModule()

val BroadcastModule = module {
    platformBroadcastModule()
}
