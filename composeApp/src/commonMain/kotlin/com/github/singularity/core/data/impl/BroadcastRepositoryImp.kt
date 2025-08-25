package com.github.singularity.core.data.impl

import com.github.singularity.core.data.BroadcastRepository
import com.github.singularity.core.database.HostedSyncGroupsRepository

class BroadcastRepositoryImp(
    private val hostedSyncGroupsRepos: HostedSyncGroupsRepository,
) : BroadcastRepository {
}
