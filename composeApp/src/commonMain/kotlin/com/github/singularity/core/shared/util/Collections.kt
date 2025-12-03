package com.github.singularity.core.shared.util

fun <T> List<T>.replaceFirstWith(newItem: (T) -> T, predicate: (T) -> Boolean): List<T> {
    val list = this.toMutableList()
    val index = indexOfFirst(predicate)
    if (index != -1) list[index] = newItem(list[index])
    return list.toList()
}

fun <K, V> Map<K?, V?>.filterNotNull(): Map<K, V> =
	mapNotNull { (k, v) -> k?.let { key -> v?.let { value -> key to value } } }
		.toMap()
