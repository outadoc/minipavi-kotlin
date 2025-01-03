package fr.outadoc.minipavi.core.internal.data

import fr.outadoc.minipavi.core.internal.data.model.GatewayRequestDTO
import fr.outadoc.minipavi.core.internal.data.model.ServiceResponseDTO
import fr.outadoc.minipavi.core.model.GatewayRequest
import fr.outadoc.minipavi.core.model.ServiceResponse
import io.ktor.server.application.ApplicationEnvironment
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

internal fun <T : Any> ServiceResponse<T>.mapToDTO(
    stateSerializer: KSerializer<T>,
    version: String
): ServiceResponseDTO {
    return ServiceResponseDTO(
        version = version,
        content = content,
        context = Json.encodeToString(stateSerializer, state),
        echo = echo.mapToDTO(),
        directCall = directCall.mapToDTO(),
        nextUrl = nextUrl,
        command = command?.mapToDTO()
    )
}

internal fun ServiceResponse.Command.mapToDTO(): ServiceResponseDTO.Command {
    return when (this) {
        is ServiceResponse.Command.BackgroundCall -> {
            ServiceResponseDTO.Command.BackgroundCall(
                params = ServiceResponseDTO.Command.BackgroundCall.Params(
                    sendAt = sendAt,
                    url = url,
                    simulate = simulate,
                    uniqueIds = uniqueIds
                )
            )
        }

        is ServiceResponse.Command.ConnectToExt -> {
            ServiceResponseDTO.Command.ConnectToExt(
                params = ServiceResponseDTO.Command.ConnectToExt.Params(
                    key = key,
                    telNumber = telNumber,
                    rx = rx,
                    tx = tx
                )
            )
        }

        is ServiceResponse.Command.ConnectToTelnet -> {
            ServiceResponseDTO.Command.ConnectToTelnet(
                params = ServiceResponseDTO.Command.ConnectToTelnet.Params(
                    host = host,
                    key = key,
                    echo = echo.mapToDTO(),
                    case = case.mapToDTO(),
                    startSequence = startSequence,
                )
            )
        }

        is ServiceResponse.Command.ConnectToWebSocket -> {
            ServiceResponseDTO.Command.ConnectToWebSocket(
                params = ServiceResponseDTO.Command.ConnectToWebSocket.Params(
                    key = key,
                    echo = echo.mapToDTO(),
                    case = case.mapToDTO(),
                    host = host,
                    path = path,
                    proto = proto,
                )
            )
        }

        is ServiceResponse.Command.Disconnect -> {
            ServiceResponseDTO.Command.Disconnect
        }

        is ServiceResponse.Command.DuplicateStream -> {
            ServiceResponseDTO.Command.DuplicateStream(
                params = ServiceResponseDTO.Command.DuplicateStream.Params(
                    key = key,
                    uniqueId = uniqueId
                )
            )
        }

        is ServiceResponse.Command.InputForm -> {
            ServiceResponseDTO.Command.InputForm(
                params = ServiceResponseDTO.Command.InputForm.Params(
                    x = cols,
                    y = lines,
                    length = length,
                    spaceChar = spaceChar,
                    prefill = prefill,
                    cursor = cursor.mapToDTO(),
                    submitWith = submitWith.map { it.mapToDTO() }.toSet()
                )
            )
        }

        is ServiceResponse.Command.InputMessage -> {
            ServiceResponseDTO.Command.InputMessage(
                params = ServiceResponseDTO.Command.InputMessage.Params(
                    x = col,
                    y = line,
                    width = width,
                    height = height,
                    spaceChar = spaceChar,
                    prefill = initialValues,
                    cursor = cursor.mapToDTO(),
                    submitWith = submitWith.map { it.mapToDTO() }.toSet()
                )
            )
        }

        is ServiceResponse.Command.InputText -> {
            ServiceResponseDTO.Command.InputText(
                params = ServiceResponseDTO.Command.InputText.Params(
                    x = col,
                    y = line,
                    length = length,
                    char = substituteChar,
                    spaceChar = spaceChar,
                    prefill = prefill,
                    cursor = cursor.mapToDTO(),
                    submitWith = submitWith.map { it.mapToDTO() }.toSet()
                )
            )
        }

        is ServiceResponse.Command.PushServiceMessage -> {
            ServiceResponseDTO.Command.PushServiceMessage(
                params = ServiceResponseDTO.Command.PushServiceMessage.Params(
                    uniqueIds = uniqueIds,
                    message = message
                )
            )
        }
    }
}

internal fun ServiceResponse.Command.FunctionKey.mapToDTO(): ServiceResponseDTO.Command.FunctionKey {
    return when (this) {
        ServiceResponse.Command.FunctionKey.Sommaire -> ServiceResponseDTO.Command.FunctionKey.SOMMAIRE
        ServiceResponse.Command.FunctionKey.Retour -> ServiceResponseDTO.Command.FunctionKey.RETOUR
        ServiceResponse.Command.FunctionKey.Repetition -> ServiceResponseDTO.Command.FunctionKey.REPETITION
        ServiceResponse.Command.FunctionKey.Guide -> ServiceResponseDTO.Command.FunctionKey.GUIDE
        ServiceResponse.Command.FunctionKey.Suite -> ServiceResponseDTO.Command.FunctionKey.SUITE
        ServiceResponse.Command.FunctionKey.Envoi -> ServiceResponseDTO.Command.FunctionKey.ENVOI
    }
}

internal fun ServiceResponse.Command.Case.mapToDTO(): ServiceResponseDTO.Command.Case {
    return when (this) {
        ServiceResponse.Command.Case.Lower -> ServiceResponseDTO.Command.Case.LOWER
        ServiceResponse.Command.Case.Upper -> ServiceResponseDTO.Command.Case.UPPER
    }
}

internal fun Boolean.mapToDTO(): ServiceResponseDTO.Command.OnOff {
    return if (this) {
        ServiceResponseDTO.Command.OnOff.ON
    } else {
        ServiceResponseDTO.Command.OnOff.OFF
    }
}

internal fun ServiceResponse.DirectCallSetting.mapToDTO(): ServiceResponseDTO.DirectCallSetting {
    return when (this) {
        ServiceResponse.DirectCallSetting.Yes -> ServiceResponseDTO.DirectCallSetting.YES
        ServiceResponse.DirectCallSetting.No -> ServiceResponseDTO.DirectCallSetting.NO
        ServiceResponse.DirectCallSetting.YesCnx -> ServiceResponseDTO.DirectCallSetting.YES_CNX
    }
}

internal fun <T : Any> GatewayRequestDTO.mapToDomain(
    environment: ApplicationEnvironment,
    serializer: KSerializer<T>,
    initialState: () -> T
): GatewayRequest<T> {
    return GatewayRequest(
        gatewayVersion = payload.version,
        sessionId = payload.uniqueId,
        remoteAddress = payload.remoteAddress,
        socketType = payload.socketType.mapToDomain(),
        minitelVersion = payload.minitelVersion,
        userInput = payload.content,
        state = try {
            payload.context
                .takeIf { context -> context.isNotEmpty() }
                ?.let { context -> Json.decodeFromString(serializer, context) }
                ?: initialState()
        } catch (e: Exception) {
            environment.log.error("Failed to decode state. Context was ${payload.context}", e)
            initialState()
        },
        function = payload.function.mapToDomain(),
        urlParams = urlParams
    )
}

internal fun GatewayRequestDTO.Function.mapToDomain(): GatewayRequest.Function {
    return when (this) {
        GatewayRequestDTO.Function.ENVOI -> GatewayRequest.Function.Envoi
        GatewayRequestDTO.Function.SUITE -> GatewayRequest.Function.Suite
        GatewayRequestDTO.Function.RETOUR -> GatewayRequest.Function.Retour
        GatewayRequestDTO.Function.ANNULATION -> GatewayRequest.Function.Annulation
        GatewayRequestDTO.Function.CORRECTION -> GatewayRequest.Function.Correction
        GatewayRequestDTO.Function.GUIDE -> GatewayRequest.Function.Guide
        GatewayRequestDTO.Function.REPETITION -> GatewayRequest.Function.Repetition
        GatewayRequestDTO.Function.SOMMAIRE -> GatewayRequest.Function.Sommaire
        GatewayRequestDTO.Function.CONNECTION -> GatewayRequest.Function.Connection
        GatewayRequestDTO.Function.FIN -> GatewayRequest.Function.Fin
        GatewayRequestDTO.Function.DIRECT -> GatewayRequest.Function.Direct
        GatewayRequestDTO.Function.DIRECT_CONNECTION -> GatewayRequest.Function.DirectConnection
        GatewayRequestDTO.Function.DIRECT_CALL_FAILED -> GatewayRequest.Function.DirectCallFailed
        GatewayRequestDTO.Function.DIRECT_CALL_ENDED -> GatewayRequest.Function.DirectCallEnded
        GatewayRequestDTO.Function.BACKGROUND_CALL -> GatewayRequest.Function.BackgroundCall
        GatewayRequestDTO.Function.SIMULATED_BACKGROUND_CALL -> GatewayRequest.Function.BackgroundCallSimulated
    }
}

internal fun GatewayRequestDTO.SocketType.mapToDomain(): GatewayRequest.SocketType {
    return when (this) {
        GatewayRequestDTO.SocketType.WebSocketSSL -> GatewayRequest.SocketType.WebSocketSSL
        GatewayRequestDTO.SocketType.WebSocket -> GatewayRequest.SocketType.WebSocket
        GatewayRequestDTO.SocketType.Asterisk -> GatewayRequest.SocketType.Asterisk
        GatewayRequestDTO.SocketType.Telnet -> GatewayRequest.SocketType.Telnet
    }
}
