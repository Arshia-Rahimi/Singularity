package com.github.singularity.app

import androidx.compose.runtime.Composable

@Composable
actual fun AppWindow(content: @Composable (() -> Unit)) = content()
