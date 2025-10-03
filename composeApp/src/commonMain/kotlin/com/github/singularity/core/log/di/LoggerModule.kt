package com.github.singularity.core.log.di

import com.github.singularity.core.log.Logger
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun Module.logger(): KoinDefinition<out Logger>

val LoggerModule = module {
    logger()
}
