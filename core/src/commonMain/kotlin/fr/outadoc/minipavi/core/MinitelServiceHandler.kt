package fr.outadoc.minipavi.core

import fr.outadoc.minipavi.core.internal.KJson
import fr.outadoc.minipavi.core.internal.data.mapToDTO
import fr.outadoc.minipavi.core.internal.data.mapToDomain
import fr.outadoc.minipavi.core.internal.data.model.GatewayRequestDTO
import fr.outadoc.minipavi.core.internal.data.model.ServiceResponseDTO
import fr.outadoc.minipavi.core.model.GatewayRequest
import fr.outadoc.minipavi.core.model.ServiceResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * Point d'entrée indépendant de la plateforme pour un service Minitel.
 *
 * Cette classe transforme le corps d'une requête envoyée par la passerelle MiniPavi
 * en [GatewayRequest], appelle [block], puis sérialise la [ServiceResponse] obtenue
 * en JSON, prêt à être renvoyé à la passerelle.
 *
 * Elle ne dépend d'aucune pile HTTP : utilisez-la directement, ou via les modules
 * d'intégration (`core-ktor` pour Ktor, `core-cloudflare` pour Cloudflare Workers).
 *
 * @param version La version du service. Par exemple, `0.1`.
 * @param stateSerializer Le [KSerializer] qui servira à sérialiser les états de type [TState].
 * @param initialState L'état initial du service, lors de la première session d'un nouvel utilisateur.
 * @param logger Le [MinitelLogger] qui recevra les messages de diagnostic.
 * @param block Le bloc de code qui sera exécuté à chaque requête de la passerelle.
 */
public class MinitelServiceHandler<TState : Any>(
    private val version: String,
    private val stateSerializer: KSerializer<TState>,
    private val initialState: () -> TState,
    private val logger: MinitelLogger = MinitelLogger.None,
    private val block: (GatewayRequest<TState>) -> ServiceResponse<TState>,
) {
    /**
     * Traite une requête de la passerelle.
     *
     * @param requestBody Le corps JSON de la requête envoyée par la passerelle.
     * @return Le corps JSON de la réponse à renvoyer à la passerelle.
     * @throws InvalidGatewayRequestException si [requestBody] n'est pas une requête valide.
     */
    public fun handle(requestBody: String): String {
        val requestDto: GatewayRequestDTO =
            try {
                KJson.decodeFromString(GatewayRequestDTO.serializer(), requestBody)
            } catch (e: IllegalArgumentException) {
                // SerializationException hérite de IllegalArgumentException
                throw InvalidGatewayRequestException(e)
            }

        val request: GatewayRequest<TState> =
            requestDto.mapToDomain(
                serializer = stateSerializer,
                initialState = initialState,
                logger = logger,
            )

        logger.log(MinitelLogger.Level.Debug) { "Received request: $request" }

        val response: ServiceResponse<TState> = block(request)

        logger.log(MinitelLogger.Level.Debug) { "Responding with: $response" }

        return KJson.encodeToString(
            ServiceResponseDTO.serializer(),
            response.mapToDTO(
                stateSerializer = stateSerializer,
                version = version,
            ),
        )
    }
}

/**
 * Crée un [MinitelServiceHandler] dont le sérialiseur d'état est déduit de [TState].
 *
 * @param version La version du service. Par exemple, `0.1`.
 * @param initialState L'état initial du service, lors de la première session d'un nouvel utilisateur.
 * @param logger Le [MinitelLogger] qui recevra les messages de diagnostic.
 * @param block Le bloc de code qui sera exécuté à chaque requête de la passerelle.
 */
public inline fun <reified TState : Any> MinitelServiceHandler(
    version: String,
    noinline initialState: () -> TState,
    logger: MinitelLogger = MinitelLogger.None,
    noinline block: (GatewayRequest<TState>) -> ServiceResponse<TState>,
): MinitelServiceHandler<TState> =
    MinitelServiceHandler(
        version = version,
        stateSerializer = serializer<TState>(),
        initialState = initialState,
        logger = logger,
        block = block,
    )
