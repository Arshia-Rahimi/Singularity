package com.github.singularity.ui.feature.home.client.pages.joinedgroup

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinedSyncGroupPage() {
    val viewModel = koinViewModel<JoinedGroupViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Text(uiState.currentGroup?.syncGroupName ?: "null")
}
