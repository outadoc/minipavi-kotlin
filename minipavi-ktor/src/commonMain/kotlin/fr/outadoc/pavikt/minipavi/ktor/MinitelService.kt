package fr.outadoc.pavikt.minipavi.ktor

import fr.outadoc.pavikt.minipavi.data.mapToDTO
import fr.outadoc.pavikt.minipavi.data.mapToDomain
import fr.outadoc.pavikt.minipavi.data.model.GatewayRequestDTO
import fr.outadoc.pavikt.minipavi.domain.model.GatewayRequest
import fr.outadoc.pavikt.minipavi.domain.model.ServiceResponse
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

public inline fun <reified T : Any> Route.minitelService(
    path: String = "/",
    version: String,
    initialState: T,
    crossinline block: Route.(GatewayRequest<T>) -> ServiceResponse<T>
) {
    post(path) {
        val requestDto: GatewayRequestDTO = call.receive<GatewayRequestDTO>()
        val request = requestDto.mapToDomain<T>(initialState = initialState)

        application.environment.log.debug("Received request: {}", request)

        val response: ServiceResponse<T> = block(request)

        application.environment.log.debug("Responding with: {}", response)

        call.respond(
            response.mapToDTO(version = version)
        )
    }
}
