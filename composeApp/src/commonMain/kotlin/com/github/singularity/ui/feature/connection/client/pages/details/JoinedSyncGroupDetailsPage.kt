package com.github.singularity.ui.feature.connection.client.pages.details

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.github.singularity.core.shared.model.JoinedSyncGroup
import com.github.singularity.ui.designsystem.components.TopBar
import com.github.singularity.ui.feature.connection.client.ClientIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinedSyncGroupDetailsPage(
	group: JoinedSyncGroup,
	execute: ClientIntent.() -> Unit,
) {
	Scaffold(
		topBar = {
			TopBar(
				title = group.syncGroupName,
			)
		},
	) { ip ->

	}
}
