package com.github.singularity.ui.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun Grid(
	ip: PaddingValues,
	content: LazyGridScope.() -> Unit,
) {
	LazyVerticalGrid(
		columns = GridCells.Adaptive(200.dp),
		modifier = Modifier.fillMaxSize()
			.padding(ip)
			.padding(horizontal = 4.dp),
	) {
		content()
	}
}
