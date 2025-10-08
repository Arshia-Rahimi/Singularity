package com.github.singularity.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.awt.ComposeWindow
import java.awt.Frame
import java.awt.Rectangle
import java.awt.Toolkit

class WindowController(private val window: ComposeWindow) {

    var isMaximized by mutableStateOf(false)
        private set

    var isFullScreen by mutableStateOf(false)
        private set

    fun fullScreen() {
        window.extendedState = Frame.MAXIMIZED_BOTH
        isMaximized = false
        isFullScreen = true
    }

    fun restore() {
        window.extendedState = Frame.NORMAL
        isMaximized = false
        isFullScreen = false
    }

    fun maximize() {
        val bounds: Rectangle = window.graphicsConfiguration.bounds
        val insets = Toolkit.getDefaultToolkit().getScreenInsets(window.graphicsConfiguration)

        val workX = bounds.x + insets.left
        val workY = bounds.y + insets.top
        val workWidth = bounds.width - insets.left - insets.right
        val workHeight = bounds.height - insets.top - insets.bottom

        window.setBounds(workX, workY, workWidth, workHeight)
        isFullScreen = false
        isMaximized = true
    }

    fun minimize() {
        window.extendedState = Frame.ICONIFIED
    }

    fun close() {
        window.dispose()
    }

}
