package com.github.singularity.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.singularity.app.di.ServerModules
import com.github.singularity.app.di.initKoin
import com.github.singularity.core.shared.compose.getString
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.singularity

fun main() = application {
    initKoin(ServerModules)
    Window(
        onCloseRequest = ::exitApplication,
        title = Res.string.singularity.getString(),
    ) {
        App()
    }
}
