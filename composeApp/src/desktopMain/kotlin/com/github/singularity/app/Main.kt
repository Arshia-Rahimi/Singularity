package com.github.singularity.app

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.koin.compose.koinInject
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import java.awt.Dimension

private val DesktopModule = module {
    viewModelOf(::DesktopViewModel)
}

fun main() = application {
    initKoin {
        modules(DesktopModule)
    }

    val viewModel = koinInject<DesktopViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    uiState?.let {
        Window(
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(width = 800.dp, height = 600.dp)
        ) {
            window.minimumSize = Dimension(500, 350)

            App()
        }
    }
}
