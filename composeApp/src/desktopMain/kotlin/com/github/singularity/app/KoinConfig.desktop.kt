package com.github.singularity.app

import com.github.singularity.core.data.di.DesktopDataModule
import com.github.singularity.core.datasource.di.DesktopDataSourceModule
import com.github.singularity.ui.di.DesktopViewModelModule

actual val PlatformModules = listOf(
	DesktopDataModule,
	DesktopDataSourceModule,
	DesktopViewModelModule,
)
