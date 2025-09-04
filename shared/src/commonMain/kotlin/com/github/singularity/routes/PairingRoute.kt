package com.github.singularity.routes

import com.github.singularity.authentication.AuthRepository
import com.github.singularity.data.HostedSyncGroupDataSource
import com.github.singularity.models.http.PairRequestModel
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.application.Application
import io.ktor.server.request.receiveParameters
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.koin.mp.KoinPlatform.getKoin

fun Application.registerPairingRoute() {
    val authRepo by getKoin().inject<AuthRepository>()
    val hostedSyncGroupsRepo by getKoin().inject<HostedSyncGroupDataSource>()
    
    routing {
        post("/pair") {
            val pairRequest = call.receiveParameters().getPairRequest()
            if (!pairRequest.isValid()) {
                call.response.status(HttpStatusCode.BadRequest)
            }
        }
    }
}

private fun Parameters.getPairRequest() = PairRequestModel(
    deviceId = this["deviceId"],
    deviceOs = this["deviceOs"],
    deviceName = this["deviceName"],
    syncGroupId = this["syncGroupId"],
    syncGroupName = this["syncGroupName"],
)
