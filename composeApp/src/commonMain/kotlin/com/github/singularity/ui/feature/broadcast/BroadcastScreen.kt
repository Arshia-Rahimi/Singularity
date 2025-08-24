package com.github.singularity.ui.feature.broadcast

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BroadcastScreen(
    navBack: () -> Unit,
) {
    val viewModel = koinViewModel<BroadcastViewModel>()

}
