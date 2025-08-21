package com.github.singularity.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.singularity.app.di.initKoin
import org.jetbrains.compose.resources.stringResource
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.singularity

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.singularity),
    ) {
        App()
    }
}
