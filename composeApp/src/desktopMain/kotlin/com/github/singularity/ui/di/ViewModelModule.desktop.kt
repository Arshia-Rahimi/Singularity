package com.github.singularity.ui.di

import com.github.singularity.ui.feature.connection.ConnectionViewModel
import com.github.singularity.ui.feature.connection.server.ServerViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val DesktopViewModelModule = module {
	viewModelOf(::ConnectionViewModel)
	viewModelOf(::ServerViewModel)
}
