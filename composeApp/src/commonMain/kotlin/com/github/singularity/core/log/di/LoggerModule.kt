package com.github.singularity.core.log.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect fun Module.platformLoggerModule()

val LoggerModule = module {
    platformLoggerModule()
}
