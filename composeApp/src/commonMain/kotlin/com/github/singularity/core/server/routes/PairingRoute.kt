package com.github.singularity.core.server.routes

import com.github.singularity.core.data.AuthRepository
import com.github.singularity.core.shared.model.http.PairRequest
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.pairingRoute(authRepo: AuthRepository) {
    post("/pair") {
        val pairRequest = call.receive<PairRequest>()
        // todo
    }
}
