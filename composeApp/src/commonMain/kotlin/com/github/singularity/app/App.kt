package com.github.singularity.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.ui.designsystem.theme.SingularityTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    val mainViewModel = koinInject<MainViewModel>()
    val theme by mainViewModel.appTheme.collectAsStateWithLifecycle()

    SingularityTheme(theme) {

    }
}
