package com.github.singularity.core.server

import com.github.singularity.core.data.AuthRepository
import com.github.singularity.core.shared.SERVER_PORT
import com.github.singularity.core.shared.model.http.PairRequest
import com.github.singularity.core.shared.model.http.PairResponse
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.bearer
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.CoroutineScope

class KtorHttpServer(
    private val authRepo: AuthRepository,
    scope: CoroutineScope,
) {

    private val server = scope.embeddedServer(
        factory = CIO,
        port = SERVER_PORT,
        host = "0.0.0.0",
        module = {
            install(Authentication) {
                bearer {
                    authenticate { authRepo.getNode(it.token) }
                }
            }
            registerRoutes()
        },
    )

    fun start() {
        server.start()
    }

    fun stop() {
        server.stop()
    }

    private fun Application.registerRoutes() {
        routing {
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
    }

}
