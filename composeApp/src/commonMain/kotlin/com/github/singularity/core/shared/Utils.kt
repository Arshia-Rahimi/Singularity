package com.github.singularity.core.shared

val scaleOptions = (0..12).map {
	(it * 0.125f) + 0.5f
}

fun Float.getScaleLabel() = "${(this * 100).cleanString()}%"

private fun Float.cleanString() = (if (this % 1 == 0f) toInt() else this).toString()
