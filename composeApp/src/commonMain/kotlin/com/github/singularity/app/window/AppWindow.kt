package com.github.singularity.app.window

import androidx.compose.runtime.Composable

@Composable
expect fun AppWindow(content: @Composable () -> Unit)
