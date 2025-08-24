package com.github.singularity.ui.feature.publish

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PublishScreen(
    navBack: () -> Unit,
) {
    val viewModel = koinViewModel<PublishViewModel>()

}
