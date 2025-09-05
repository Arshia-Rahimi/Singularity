package com.github.singularity.core.server.routes

import com.github.singularity.core.data.AuthRepository
import com.github.singularity.core.shared.model.http.PairRequest
import com.github.singularity.core.shared.model.http.PairResponse
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.pairingRoute(authRepo: AuthRepository) {
    post("/pair") {
        val pairRequest = call.receive<PairRequest>()
        val node = authRepo.authenticate(pairRequest)

        val response = when (node) {
            null -> PairResponse(
                success = false,
                message = "Denied to join server ${pairRequest.syncGroupName}",
            )

            else -> PairResponse(
                success = true,
                node = node,
            )
        }

        call.respond(response)
    }
}
