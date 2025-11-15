package com.github.singularity.core.log.di

import com.github.singularity.core.log.DesktopLogger
import com.github.singularity.core.log.Logger
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

actual fun Module.platformLoggerModule() {
    singleOf(::DesktopLogger) bind Logger::class
}
