package fr.outadoc.pavikt.minipavi.ktor

import fr.outadoc.pavikt.minipavi.internal.data.mapToDTO
import fr.outadoc.pavikt.minipavi.internal.data.mapToDomain
import fr.outadoc.pavikt.minipavi.internal.data.model.GatewayRequestDTO
import fr.outadoc.pavikt.minipavi.model.GatewayRequest
import fr.outadoc.pavikt.minipavi.model.ServiceResponse
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

@OptIn(InternalSerializationApi::class)
public inline fun <reified T : Any> Application.minitelService(
    path: String,
    version: String,
    initialState: T,
    noinline block: Route.(GatewayRequest<T>) -> ServiceResponse<T>
) {
    minitelService(
        path = path,
        version = version,
        stateSerializer = T::class.serializer(),
        initialState = initialState,
        block = block
    )
}

public fun <T : Any> Application.minitelService(
    path: String,
    version: String,
    stateSerializer: KSerializer<T>,
    initialState: T,
    block: Route.(GatewayRequest<T>) -> ServiceResponse<T>
) {
    install(ContentNegotiation) {
        json(Json)
    }

    routing {
        post(path) {
            val requestDto: GatewayRequestDTO = call.receive<GatewayRequestDTO>()
            val request = requestDto.mapToDomain(
                serializer = stateSerializer,
                initialState = initialState
            )

            application.environment.log.debug("Received request: {}", request)

            val response: ServiceResponse<T> = block(request)

            application.environment.log.debug("Responding with: {}", response)

            call.respond(
                response.mapToDTO(
                    stateSerializer = stateSerializer,
                    version = version
                )
            )
        }
    }
}
