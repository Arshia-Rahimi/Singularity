package com.github.singularity.core.server.routes

import com.github.singularity.core.data.AuthRepository
import com.github.singularity.core.shared.model.http.PairRequest
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.registerPairingRoute(authRepo: AuthRepository) {
    routing {
        post("/pair") {
            val pairRequest = call.receive<PairRequest>()
            // todo
        }
    }
}
