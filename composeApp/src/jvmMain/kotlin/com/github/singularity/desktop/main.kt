package com.github.singularity.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.singularity.common.app.App
import com.github.singularity.common.app.di.initKoin

fun main() {
    initKoin()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Singularity",
        ) {
            App()
        }
    }
}
