package com.github.singularity.ui.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LinearLoader() {
	Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
		LinearProgressIndicator()
	}
}
