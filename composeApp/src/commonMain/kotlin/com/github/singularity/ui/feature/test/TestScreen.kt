package com.github.singularity.ui.feature.test

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.ui.designsystem.components.TopBar
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen() {
    val viewModel = koinViewModel<TestViewModel>()
    val events by viewModel.incomingEvents.collectAsStateWithLifecycle()

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = { TopBar("test") },
	) {
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

}
