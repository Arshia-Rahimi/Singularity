package com.github.singularity.ui.designsystem.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
	title: @Composable () -> Unit = {},
	modifier: Modifier = Modifier,
	actions: @Composable RowScope.() -> Unit = {},
	expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
	windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
	colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
	scrollBehavior: TopAppBarScrollBehavior? = null,
) {
	TopAppBar(
		title = title,
		modifier = modifier,
		actions = actions,
		expandedHeight = expandedHeight,
		windowInsets = windowInsets,
		colors = colors,
		scrollBehavior = scrollBehavior,
		navigationIcon = { DrawerIcon() },
	)
}
