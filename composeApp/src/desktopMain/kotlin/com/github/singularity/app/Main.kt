package com.github.singularity.app

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        undecorated = true,
        state = rememberWindowState(width = 800.dp, height = 600.dp)
    ) {
        window.minimumSize = Dimension(500, 350)

        CompositionLocalProvider(LocalWindowController provides WindowController(window)) {
            App()
        }
    }
}
