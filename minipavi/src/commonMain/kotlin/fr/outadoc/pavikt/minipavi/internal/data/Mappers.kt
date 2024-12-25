package fr.outadoc.pavikt.minipavi.internal.data

import fr.outadoc.pavikt.minipavi.internal.data.model.CommandDTO
import fr.outadoc.pavikt.minipavi.internal.data.model.GatewayRequestDTO
import fr.outadoc.pavikt.minipavi.internal.data.model.ServiceResponseDTO
import fr.outadoc.pavikt.minipavi.model.Command
import fr.outadoc.pavikt.minipavi.model.GatewayRequest
import fr.outadoc.pavikt.minipavi.model.ServiceResponse
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
        next = next,
        command = command?.mapToDTO()
    )
}

internal fun Command.mapToDTO(): CommandDTO {
    return when (this) {
        is Command.BackgroundCall -> {
            CommandDTO.BackgroundCall(
                params = CommandDTO.BackgroundCall.Params(
                    sendAt = sendAt,
                    url = url,
                    simulate = simulate,
                    uniqueIds = uniqueIds
                )
            )
        }

        is Command.ConnectToExt -> {
            CommandDTO.ConnectToExt(
                params = CommandDTO.ConnectToExt.Params(
                    key = key,
                    telNumber = telNumber,
                    rx = rx,
                    tx = tx
                )
            )
        }

        is Command.ConnectToTelnet -> {
            CommandDTO.ConnectToTelnet(
                params = CommandDTO.ConnectToTelnet.Params(
                    host = host,
                    key = key,
                    echo = echo.mapToDTO(),
                    case = case.mapToDTO(),
                    startSequence = startSequence,
                )
            )
        }

        is Command.ConnectToWebSocket -> {
            CommandDTO.ConnectToWebSocket(
                params = CommandDTO.ConnectToWebSocket.Params(
                    key = key,
                    echo = echo.mapToDTO(),
                    case = case.mapToDTO(),
                    host = host,
                    path = path,
                    proto = proto,
                )
            )
        }

        is Command.Disconnect -> {
            CommandDTO.Disconnect
        }

        is Command.DuplicateStream -> {
            CommandDTO.DuplicateStream(
                params = CommandDTO.DuplicateStream.Params(
                    key = key,
                    uniqueId = uniqueId
                )
            )
        }

        is Command.InputForm -> {
            CommandDTO.InputForm(
                params = CommandDTO.InputForm.Params(
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

        is Command.InputMessage -> {
            CommandDTO.InputMessage(
                params = CommandDTO.InputMessage.Params(
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

        is Command.InputText -> {
            CommandDTO.InputText(
                params = CommandDTO.InputText.Params(
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

        is Command.PushServiceMessage -> {
            CommandDTO.PushServiceMessage(
                params = CommandDTO.PushServiceMessage.Params(
                    uniqueIds = uniqueIds,
                    message = message
                )
            )
        }
    }
}

internal fun Command.FunctionKey.mapToDTO(): CommandDTO.FunctionKey {
    return when (this) {
        Command.FunctionKey.SOMMAIRE -> CommandDTO.FunctionKey.SOMMAIRE
        Command.FunctionKey.RETOUR -> CommandDTO.FunctionKey.RETOUR
        Command.FunctionKey.REPETITION -> CommandDTO.FunctionKey.REPETITION
        Command.FunctionKey.GUIDE -> CommandDTO.FunctionKey.GUIDE
        Command.FunctionKey.SUITE -> CommandDTO.FunctionKey.SUITE
        Command.FunctionKey.ENVOI -> CommandDTO.FunctionKey.ENVOI
    }
}

internal fun Command.Case.mapToDTO(): CommandDTO.Case {
    return when (this) {
        Command.Case.LOWER -> CommandDTO.Case.LOWER
        Command.Case.UPPER -> CommandDTO.Case.UPPER
    }
}

internal fun Boolean.mapToDTO(): CommandDTO.OnOff {
    return if (this) {
        CommandDTO.OnOff.ON
    } else {
        CommandDTO.OnOff.OFF
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
