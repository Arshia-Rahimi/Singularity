package com.github.singularity.ui.feature.test

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TestScreen() {
    val viewModel = koinViewModel<TestViewModel>()
    val events by viewModel.incomingEvents.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        stickyHeader {
            Button(
                onClick = viewModel::sendEvent,
            ) {
                Text("sendEvent")
            }
        }
        items(events) {
            Text(it.name, color = MaterialTheme.colorScheme.onPrimary)
        }
    }

}
