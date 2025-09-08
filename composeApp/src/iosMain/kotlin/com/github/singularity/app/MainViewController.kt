package com.github.singularity.app

import androidx.compose.ui.window.ComposeUIViewController
import com.github.singularity.app.di.ClientOnlyModules
import com.github.singularity.app.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin(ClientOnlyModules) },
) {
    App()
}
