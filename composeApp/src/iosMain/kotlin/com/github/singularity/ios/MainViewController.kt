package com.github.singularity.ios

import androidx.compose.ui.window.ComposeUIViewController
import com.github.singularity.common.app.App
import com.github.singularity.common.app.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() },
) {
    App()
}
