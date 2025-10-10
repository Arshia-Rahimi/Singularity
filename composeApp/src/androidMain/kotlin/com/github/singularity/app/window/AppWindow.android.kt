package com.github.singularity.app.window

import androidx.compose.runtime.Composable

@Composable
actual fun AppWindow(content: @Composable (() -> Unit)) = content()
