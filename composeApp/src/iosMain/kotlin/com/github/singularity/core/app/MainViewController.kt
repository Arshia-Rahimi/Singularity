package com.github.singularity.core.app

import androidx.compose.ui.window.ComposeUIViewController
import com.github.singularity.app.App
import com.github.singularity.app.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() },
) {
    App()
}
