package fr.outadoc.minipavi.core.ktor

import fr.outadoc.minipavi.core.internal.data.mapToDTO
import fr.outadoc.minipavi.core.internal.data.mapToDomain
import fr.outadoc.minipavi.core.internal.data.model.GatewayRequestDTO
import fr.outadoc.minipavi.core.model.GatewayRequest
import fr.outadoc.minipavi.core.model.ServiceResponse
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

/**
 * Définit la route d'un nouveau service Minitel.
 *
 * Utilisez cette fonction comme point d'entrée pour votre service.
 * Chaque requête de la passerelle MiniPavi à votre service entrainera un appel à [block],
 * où vous pouvez traiter la requête et renvoyer une [ServiceResponse] appropriée.
 *
 * @param path La route par défaut du service qui sera appelé par la passerelle.
 * Par exemple, `/`, ou `/mon-service`.
 * @param version La version du service. Par exemple, `0.1`.
 * @param initialState L'état initial du service, lors de la première session d'un nouvel utilisateur.
 * @param block Le bloc de code qui sera exécuté à chaque requête de la passerelle.
 */
@OptIn(InternalSerializationApi::class)
@Suppress("DEPRECATION")
public inline fun <reified TState : Any> Application.minitelService(
    path: String,
    version: String,
    noinline initialState: () -> TState,
    noinline block: Route.(GatewayRequest<TState>) -> ServiceResponse<TState>
) {
    minitelService(
        path = path,
        version = version,
        stateSerializer = TState::class.serializer(),
        initialState = initialState,
        block = block
    )
}

/**
 * Définit la route d'un nouveau service Minitel.
 *
 * Utilisez cette fonction comme point d'entrée pour votre service.
 * Chaque requête de la passerelle MiniPavi à votre service entrainera un appel à [block],
 * où vous pouvez traiter la requête et renvoyer une [ServiceResponse] appropriée.
 *
 * @param path La route par défaut du service qui sera appelé par la passerelle.
 * Par exemple, `/`, ou `/mon-service`.
 * @param version La version du service. Par exemple, `0.1`.
 * @param stateSerializer Le [KSerializer] qui servira à sérialiser les états de type [TState].
 * @param initialState L'état initial du service, lors de la première session d'un nouvel utilisateur.
 * @param block Le bloc de code qui sera exécuté à chaque requête de la passerelle.
 */
@Deprecated(
    message = "Utilisez plutôt la fonction inline.",
    replaceWith = ReplaceWith("minitelService(path, version, initialState, block)"),
)
public fun <TState : Any> Application.minitelService(
    path: String,
    version: String,
    stateSerializer: KSerializer<TState>,
    initialState: () -> TState,
    block: Route.(GatewayRequest<TState>) -> ServiceResponse<TState>
) {
    install(ContentNegotiation) {
        json(Json)
    }

    routing {
        post(path) {
            val requestDto: GatewayRequestDTO = call.receive<GatewayRequestDTO>()
            val request = requestDto.mapToDomain(
                environment = application.environment,
                serializer = stateSerializer,
                initialState = initialState
            )

            application.environment.log.debug("Received request: {}", request)

            val response: ServiceResponse<TState> = block(request)

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
