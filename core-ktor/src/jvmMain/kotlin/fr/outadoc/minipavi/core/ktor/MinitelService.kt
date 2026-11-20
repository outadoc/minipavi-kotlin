package fr.outadoc.minipavi.core.ktor

import fr.outadoc.minipavi.core.InvalidGatewayRequestException
import fr.outadoc.minipavi.core.MinitelLogger
import fr.outadoc.minipavi.core.MinitelServiceHandler
import fr.outadoc.minipavi.core.model.GatewayRequest
import fr.outadoc.minipavi.core.model.ServiceResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receiveText
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.KSerializer
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
@Suppress("DEPRECATION")
public inline fun <reified TState : Any> Application.minitelService(
    path: String,
    version: String,
    noinline initialState: () -> TState,
    noinline block: Route.(GatewayRequest<TState>) -> ServiceResponse<TState>,
) {
    minitelService(
        path = path,
        version = version,
        stateSerializer = serializer<TState>(),
        initialState = initialState,
        block = block,
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
    block: Route.(GatewayRequest<TState>) -> ServiceResponse<TState>,
) {
    val logger = Slf4jMinitelLogger(environment.log)

    routing {
        val route: Route = this
        val handler =
            MinitelServiceHandler(
                version = version,
                stateSerializer = stateSerializer,
                initialState = initialState,
                logger = logger,
                block = { request -> route.block(request) },
            )

        post(path) {
            val responseBody: String =
                try {
                    handler.handle(call.receiveText())
                } catch (e: InvalidGatewayRequestException) {
                    logger.log(MinitelLogger.Level.Warn, e) {
                        "Received an invalid request"
                    }
                    call.respondText(
                        text = "Bad Request",
                        status = HttpStatusCode.BadRequest,
                    )
                    return@post
                }

            call.respondText(
                text = responseBody,
                contentType = ContentType.Application.Json,
            )
        }
    }
}
