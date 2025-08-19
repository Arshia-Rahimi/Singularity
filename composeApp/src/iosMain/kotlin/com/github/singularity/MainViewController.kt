package com.github.singularity

import androidx.compose.ui.window.ComposeUIViewController
import com.github.singularity.app.App
import com.github.singularity.app.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() },
) {
    App()
}
