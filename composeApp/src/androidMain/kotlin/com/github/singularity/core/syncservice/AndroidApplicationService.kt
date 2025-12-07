package com.github.singularity.core.syncservice

import android.app.Service
import android.content.Intent
import org.koin.android.ext.android.inject

class AndroidApplicationService : Service() {

    private val service by inject<SyncService>()

    override fun onBind(intent: Intent?) = null

}
