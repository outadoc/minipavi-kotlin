package fr.outadoc.kpavi.api.server

import fr.outadoc.kpavi.api.data.model.GatewayRequest
import fr.outadoc.kpavi.api.data.model.ServiceResponse
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.mitterrand() {
    post("/") {
        val request = call.getPaviRequest()
        call.respond(
            ServiceResponse(
                version = "0.1",
                content = readResource("/static/mitterrand.vdt"),
            )
        )
    }
}

suspend fun RoutingCall.getPaviRequest(): GatewayRequest {
    return receive<GatewayRequest>().also { body ->
        application.environment.log.debug(
            "Received request: {}", body
        )
    }
}
