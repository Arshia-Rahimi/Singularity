package com.github.singularity.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.singularity.app.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Singularity",
    ) {
        App()
    }
}
