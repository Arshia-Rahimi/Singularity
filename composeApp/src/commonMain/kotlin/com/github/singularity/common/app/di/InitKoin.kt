package com.github.singularity.common.app.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(modulesList)
    }
}
