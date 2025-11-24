package com.github.singularity.core.shared

expect val deviceName: String

val scaleOptions = (0..12).map {
	(it * 0.125f) + 0.5f
}

fun Float.getScaleLabel() = "${(this * 100).cleanString()}%"

fun Float.cleanString() = (if (this % 1 == 0f) toInt() else this).toString()

fun <K, V> Map<K?, V?>.filterNotNull(): Map<K, V> =
	mapNotNull { (k, v) -> k?.let { key -> v?.let { value -> key to value } } }
		.toMap()
