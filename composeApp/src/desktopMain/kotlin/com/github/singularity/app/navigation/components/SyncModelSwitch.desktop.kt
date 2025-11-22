package com.github.singularity.app.navigation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.singularity.core.shared.SyncMode
import com.github.singularity.core.syncservice.SyncService
import com.github.singularity.ui.designsystem.shared.getString
import org.koin.compose.koinInject

@Composable
actual fun SyncModeSwitch() {
	val syncService = koinInject<SyncService>()
	val syncMode by syncService.syncMode.collectAsStateWithLifecycle()

	Row(
		modifier = Modifier.fillMaxWidth()
			.padding(4.dp),
		horizontalArrangement = Arrangement.Center,
	) {
		SingleChoiceSegmentedButtonRow {
			SyncMode.entries.forEachIndexed { index, mode ->
				val selected = syncMode == mode
				SegmentedButton(
					selected = selected,
					label = { Text(text = mode.title.getString()) },
					onClick = {
						if (selected) return@SegmentedButton
						syncService.toggleSyncMode()
					},
					shape = SegmentedButtonDefaults.itemShape(
						index = index,
						count = SyncMode.entries.size,
					)
				)
			}
		}
	}
}
