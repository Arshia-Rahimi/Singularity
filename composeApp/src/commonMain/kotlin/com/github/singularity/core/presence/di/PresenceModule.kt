package com.github.singularity.core.presence.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect fun Module.platformPresenceModule()

val PresenceModule = module {
    platformPresenceModule()
}
