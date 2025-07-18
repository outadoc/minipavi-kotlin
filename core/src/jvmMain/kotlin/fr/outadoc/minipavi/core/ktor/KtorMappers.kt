package fr.outadoc.minipavi.core.ktor

import fr.outadoc.minipavi.core.internal.KJson
import fr.outadoc.minipavi.core.internal.data.mapToDomain
import fr.outadoc.minipavi.core.internal.data.model.GatewayRequestDTO
import fr.outadoc.minipavi.core.model.GatewayRequest
import io.ktor.server.application.ApplicationEnvironment
import kotlinx.serialization.KSerializer

internal fun <TState : Any> GatewayRequestDTO.mapToDomain(
    environment: ApplicationEnvironment,
    serializer: KSerializer<TState>,
    initialState: () -> TState,
): GatewayRequest<TState> {
    return GatewayRequest(
        gatewayVersion = payload.version,
        userId = payload.uniqueId,
        remoteAddress = payload.remoteAddress,
        socketType = payload.socketType.mapToDomain(),
        minitelVersion = payload.minitelVersion,
        userInput = payload.content,
        state = try {
            payload.context
                .takeIf { context -> context.isNotEmpty() }
                ?.let { context -> KJson.decodeFromString(serializer, context) }
                ?: initialState()
        } catch (e: Exception) {
            environment.log.error("Failed to decode state. Context was ${payload.context}", e)
            initialState()
        },
        event = payload.function.mapToDomain(),
        urlParams = urlParams
    )
}
