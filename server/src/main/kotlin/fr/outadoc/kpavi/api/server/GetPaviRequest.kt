package fr.outadoc.kpavi.api.server

import fr.outadoc.kpavi.api.data.model.GatewayRequest
import fr.outadoc.kpavi.api.data.model.ServiceResponse
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

inline fun <reified T : Any> Route.miniRoute(
    path: String,
    crossinline block: Route.(GatewayRequest<T>) -> ServiceResponse<T>
) {
    post(path) {
        val request: GatewayRequest<T> = call.receive<GatewayRequest<T>>()
        application.environment.log.debug("Received request: {}", request)

        val response: ServiceResponse<T> = block(request)
        application.environment.log.debug("Responding with: {}", response)

        call.respond(response)
    }
}
