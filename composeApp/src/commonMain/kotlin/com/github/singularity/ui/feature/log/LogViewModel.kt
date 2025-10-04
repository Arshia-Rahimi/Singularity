package com.github.singularity.ui.feature.log

import androidx.lifecycle.ViewModel
import com.github.singularity.core.log.Logger
import com.github.singularity.core.shared.util.stateInWhileSubscribed

class LogViewModel(
    private val logger: Logger,
): ViewModel() {

    val logStream = logger.logStream
        .stateInWhileSubscribed("")

}