package com.github.singularity.core.shared.util

fun <T> MutableList<T>.replaceFirstWith(newItem: (T) -> T, predicate: (T) -> Boolean) {
    val index = indexOfFirst(predicate)
    if (index != -1) this[index] = newItem(this[index])
}
