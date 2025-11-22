package com.github.singularity.ui.di

import com.github.singularity.ui.feature.connection.ConnectionViewModel
import com.github.singularity.ui.feature.connection.server.ServerViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf

actual fun Module.platformViewModelModule() {
    viewModelOf(::ConnectionViewModel)
    viewModelOf(::ServerViewModel)
}
