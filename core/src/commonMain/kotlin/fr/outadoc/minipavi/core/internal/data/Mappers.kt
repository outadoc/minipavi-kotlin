package fr.outadoc.minipavi.core.internal.data

import fr.outadoc.minipavi.core.internal.data.model.GatewayRequestDTO
import fr.outadoc.minipavi.core.internal.data.model.ServiceResponseDTO
import fr.outadoc.minipavi.core.model.GatewayRequest
import fr.outadoc.minipavi.core.model.ServiceResponse
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
                    x = x,
                    y = y,
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
                    x = x,
                    y = y,
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
                    x = x,
                    y = y,
                    length = length,
                    char = char,
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
        ServiceResponse.Command.FunctionKey.SOMMAIRE -> ServiceResponseDTO.Command.FunctionKey.SOMMAIRE
        ServiceResponse.Command.FunctionKey.RETOUR -> ServiceResponseDTO.Command.FunctionKey.RETOUR
        ServiceResponse.Command.FunctionKey.REPETITION -> ServiceResponseDTO.Command.FunctionKey.REPETITION
        ServiceResponse.Command.FunctionKey.GUIDE -> ServiceResponseDTO.Command.FunctionKey.GUIDE
        ServiceResponse.Command.FunctionKey.SUITE -> ServiceResponseDTO.Command.FunctionKey.SUITE
        ServiceResponse.Command.FunctionKey.ENVOI -> ServiceResponseDTO.Command.FunctionKey.ENVOI
    }
}

internal fun ServiceResponse.Command.Case.mapToDTO(): ServiceResponseDTO.Command.Case {
    return when (this) {
        ServiceResponse.Command.Case.LOWER -> ServiceResponseDTO.Command.Case.LOWER
        ServiceResponse.Command.Case.UPPER -> ServiceResponseDTO.Command.Case.UPPER
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
        ServiceResponse.DirectCallSetting.YES -> ServiceResponseDTO.DirectCallSetting.YES
        ServiceResponse.DirectCallSetting.NO -> ServiceResponseDTO.DirectCallSetting.NO
        ServiceResponse.DirectCallSetting.YES_CNX -> ServiceResponseDTO.DirectCallSetting.YES_CNX
    }
}

internal fun <T : Any> GatewayRequestDTO.mapToDomain(
    serializer: KSerializer<T>,
    initialState: T
): GatewayRequest<T> {
    return GatewayRequest(
        payload = GatewayRequest.Payload(
            gatewayVersion = payload.version,
            sessionId = payload.uniqueId,
            remoteAddress = payload.remoteAddress,
            socketType = payload.socketType.mapToDomain(),
            minitelVersion = payload.minitelVersion,
            userInput = payload.content,
            state = payload.context
                .takeIf { context -> context.isNotEmpty() }
                ?.let { context -> Json.decodeFromString(serializer, context) }
                ?: initialState,
            function = payload.function.mapToDomain()
        ),
        urlParams = urlParams
    )
}

internal fun GatewayRequestDTO.Function.mapToDomain(): GatewayRequest.Function {
    return when (this) {
        GatewayRequestDTO.Function.ENVOI -> GatewayRequest.Function.ENVOI
        GatewayRequestDTO.Function.SUITE -> GatewayRequest.Function.SUITE
        GatewayRequestDTO.Function.RETOUR -> GatewayRequest.Function.RETOUR
        GatewayRequestDTO.Function.ANNULATION -> GatewayRequest.Function.ANNULATION
        GatewayRequestDTO.Function.CORRECTION -> GatewayRequest.Function.CORRECTION
        GatewayRequestDTO.Function.GUIDE -> GatewayRequest.Function.GUIDE
        GatewayRequestDTO.Function.REPETITION -> GatewayRequest.Function.REPETITION
        GatewayRequestDTO.Function.SOMMAIRE -> GatewayRequest.Function.SOMMAIRE
        GatewayRequestDTO.Function.CONNECTION -> GatewayRequest.Function.CONNECTION
        GatewayRequestDTO.Function.FIN -> GatewayRequest.Function.FIN
        GatewayRequestDTO.Function.DIRECT -> GatewayRequest.Function.DIRECT
        GatewayRequestDTO.Function.DIRECT_CONNECTION -> GatewayRequest.Function.DIRECT_CONNECTION
        GatewayRequestDTO.Function.DIRECT_CALL_FAILED -> GatewayRequest.Function.DIRECT_CALL_FAILED
        GatewayRequestDTO.Function.DIRECT_CALL_ENDED -> GatewayRequest.Function.DIRECT_CALL_ENDED
        GatewayRequestDTO.Function.BACKGROUND_CALL -> GatewayRequest.Function.BACKGROUND_CALL
        GatewayRequestDTO.Function.SIMULATED_BACKGROUND_CALL -> GatewayRequest.Function.SIMULATED_BACKGROUND_CALL
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
