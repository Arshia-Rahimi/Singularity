package com.github.singularity.ui.feature.home.client.pages

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.github.singularity.ui.feature.home.client.ClientIntent
import com.github.singularity.ui.feature.home.client.ClientUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinedSyncGroupPage(
    uiState: ClientUiState,
    execute: ClientIntent.() -> Unit,
    topBar: (@Composable () -> Unit) -> Unit,
) {


}
