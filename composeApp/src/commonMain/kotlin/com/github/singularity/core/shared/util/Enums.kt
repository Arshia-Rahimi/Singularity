package com.github.singularity.core.shared.util

inline fun <reified T : Enum<T>> T.next() =
    enumValues<T>().run { get((ordinal + 1) % size) }
