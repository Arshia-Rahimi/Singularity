package com.github.singularity.ui.feature.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.singularity.core.log.Logger
import com.github.singularity.core.shared.util.stateInWhileSubscribed
import kotlinx.coroutines.launch

class LogViewModel(
	private val logger: Logger,
) : ViewModel() {

	val logStream = logger.log.stateInWhileSubscribed("")

	fun clear() {
		viewModelScope.launch {
			logger.clearLog()
		}
	}

}
