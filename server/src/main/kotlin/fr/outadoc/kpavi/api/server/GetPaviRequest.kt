package fr.outadoc.kpavi.api.server

import fr.outadoc.kpavi.api.data.model.GatewayRequest
import io.ktor.server.request.*
import io.ktor.server.routing.*

suspend inline fun <reified T : Any> RoutingCall.getPaviRequest(): GatewayRequest<T> {
    return receive<GatewayRequest<T>>().also { body ->
        application.environment.log.debug(
            "Received request: {}", body
        )
    }
}