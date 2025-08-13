package com.github.singularity

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform