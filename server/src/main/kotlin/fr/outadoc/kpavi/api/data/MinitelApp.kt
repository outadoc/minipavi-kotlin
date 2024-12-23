package fr.outadoc.kpavi.api.data

import fr.outadoc.kpavi.api.data.model.GatewayRequestDTO
import fr.outadoc.kpavi.api.domain.model.GatewayRequest
import fr.outadoc.kpavi.api.domain.model.ServiceResponse
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

inline fun <reified T : Any> Route.minitelApp(
    path: String = "/",
    crossinline block: Route.(GatewayRequest<T>) -> ServiceResponse<T>
) {
    post(path) {
        val requestDto: GatewayRequestDTO = call.receive<GatewayRequestDTO>()
        val request = requestDto.mapToDomain<T>()

        application.environment.log.debug("Received request: {}", request)

        val response: ServiceResponse<T> = block(request)

        application.environment.log.debug("Responding with: {}", response)

        call.respond(response.mapToDTO())
    }
}
