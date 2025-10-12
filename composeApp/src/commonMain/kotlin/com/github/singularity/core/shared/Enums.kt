package com.github.singularity.core.shared

import org.jetbrains.compose.resources.StringResource
import singularity.composeapp.generated.resources.Res
import singularity.composeapp.generated.resources.dark
import singularity.composeapp.generated.resources.follow_system
import singularity.composeapp.generated.resources.light

enum class AppTheme(
    val title: StringResource,
) {
    Light(Res.string.light),
    Dark(Res.string.dark),
    System(Res.string.follow_system),
}

enum class SyncMode {
    Server, Client,
}

enum class ScaleOption(
	val value: Float,
	val label: String,
) {
	S50(
		value = 0.5f,
		label = "50%",
	),
	S62_5(
		value = 0.625f,
		label = "62.5%",
	),
	S75(
		value = 0.75f,
		label = "75%",
	),
	S87_5(
		value = 0.875f,
		label = "87.5%",
	),
	S100(
		value = 1f,
		label = "100%",
	),
	S112_5(
		value = 1.125f,
		label = "112.5%",
	),
	S125(
		value = 1.125f,
		label = "125%",
	),
	S137_5(
		value = 1.375f,
		label = "137.5%",
	),
	S150(
		value = 1.5f,
		label = "150%",
	),
	S162_5(
		value = 1.625f,
		label = "162.5%",
	),
	S175(
		value = 1.75f,
		label = "175%",
	),
	S187_5(
		value = 1.875f,
		label = "187.5%",
	),
	S200(
		value = 2f,
		label = "200%",
	),
}
