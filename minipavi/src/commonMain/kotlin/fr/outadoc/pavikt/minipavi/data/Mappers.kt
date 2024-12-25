package fr.outadoc.pavikt.minipavi.data

import fr.outadoc.pavikt.minipavi.data.model.CommandDTO
import fr.outadoc.pavikt.minipavi.data.model.GatewayRequestDTO
import fr.outadoc.pavikt.minipavi.data.model.ServiceResponseDTO
import fr.outadoc.pavikt.minipavi.domain.model.Command
import fr.outadoc.pavikt.minipavi.domain.model.GatewayRequest
import fr.outadoc.pavikt.minipavi.domain.model.ServiceResponse
import kotlinx.serialization.json.Json

inline fun <reified T : Any> ServiceResponse<T>.mapToDTO(version: String): ServiceResponseDTO {
    return ServiceResponseDTO(
        version = version,
        content = content,
        context = Json.encodeToString(state),
        echo = echo.mapToDTO(),
        directCall = directCall.mapToDTO(),
        next = next,
        command = command?.mapToDTO()
    )
}

fun Command.mapToDTO(): CommandDTO {
    return when (this) {
        is Command.BackgroundCall -> {
            fr.outadoc.pavikt.minipavi.data.model.CommandDTO.BackgroundCall(
                params = fr.outadoc.pavikt.minipavi.data.model.CommandDTO.BackgroundCall.Params(
                    sendAt = sendAt,
                    url = url,
                    simulate = simulate,
                    uniqueIds = uniqueIds
                )
            )
        }

        is Command.ConnectToExt -> {
            fr.outadoc.pavikt.minipavi.data.model.CommandDTO.ConnectToExt(
                params = fr.outadoc.pavikt.minipavi.data.model.CommandDTO.ConnectToExt.Params(
                    key = key,
                    telNumber = telNumber,
                    rx = rx,
                    tx = tx
                )
            )
        }

        is Command.ConnectToTelnet -> {
            fr.outadoc.pavikt.minipavi.data.model.CommandDTO.ConnectToTelnet(
                params = fr.outadoc.pavikt.minipavi.data.model.CommandDTO.ConnectToTelnet.Params(
                    host = host,
                    key = key,
                    echo = echo.mapToDTO(),
                    case = case.mapToDTO(),
                    startSequence = startSequence,
                )
            )
        }

        is Command.ConnectToWebSocket -> {
            fr.outadoc.pavikt.minipavi.data.model.CommandDTO.ConnectToWebSocket(
                params = fr.outadoc.pavikt.minipavi.data.model.CommandDTO.ConnectToWebSocket.Params(
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
            fr.outadoc.pavikt.minipavi.data.model.CommandDTO.Disconnect
        }

        is Command.DuplicateStream -> {
            fr.outadoc.pavikt.minipavi.data.model.CommandDTO.DuplicateStream(
                params = fr.outadoc.pavikt.minipavi.data.model.CommandDTO.DuplicateStream.Params(
                    key = key,
                    uniqueId = uniqueId
                )
            )
        }

        is Command.InputForm -> {
            fr.outadoc.pavikt.minipavi.data.model.CommandDTO.InputForm(
                params = fr.outadoc.pavikt.minipavi.data.model.CommandDTO.InputForm.Params(
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
            fr.outadoc.pavikt.minipavi.data.model.CommandDTO.InputMessage(
                params = fr.outadoc.pavikt.minipavi.data.model.CommandDTO.InputMessage.Params(
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
            fr.outadoc.pavikt.minipavi.data.model.CommandDTO.InputText(
                params = fr.outadoc.pavikt.minipavi.data.model.CommandDTO.InputText.Params(
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
            fr.outadoc.pavikt.minipavi.data.model.CommandDTO.PushServiceMessage(
                params = fr.outadoc.pavikt.minipavi.data.model.CommandDTO.PushServiceMessage.Params(
                    uniqueIds = uniqueIds,
                    message = message
                )
            )
        }
    }
}

fun Command.FunctionKey.mapToDTO(): CommandDTO.FunctionKey {
    return when (this) {
        Command.FunctionKey.SOMMAIRE -> fr.outadoc.pavikt.minipavi.data.model.CommandDTO.FunctionKey.SOMMAIRE
        Command.FunctionKey.RETOUR -> fr.outadoc.pavikt.minipavi.data.model.CommandDTO.FunctionKey.RETOUR
        Command.FunctionKey.REPETITION -> fr.outadoc.pavikt.minipavi.data.model.CommandDTO.FunctionKey.REPETITION
        Command.FunctionKey.GUIDE -> fr.outadoc.pavikt.minipavi.data.model.CommandDTO.FunctionKey.GUIDE
        Command.FunctionKey.SUITE -> fr.outadoc.pavikt.minipavi.data.model.CommandDTO.FunctionKey.SUITE
        Command.FunctionKey.ENVOI -> fr.outadoc.pavikt.minipavi.data.model.CommandDTO.FunctionKey.ENVOI
    }
}

fun Command.Case.mapToDTO(): CommandDTO.Case {
    return when (this) {
        Command.Case.LOWER -> fr.outadoc.pavikt.minipavi.data.model.CommandDTO.Case.LOWER
        Command.Case.UPPER -> fr.outadoc.pavikt.minipavi.data.model.CommandDTO.Case.UPPER
    }
}

fun Boolean.mapToDTO(): CommandDTO.OnOff {
    return if (this) {
        CommandDTO.OnOff.ON
    } else {
        CommandDTO.OnOff.OFF
    }
}

fun ServiceResponse.DirectCallSetting.mapToDTO(): ServiceResponseDTO.DirectCallSetting {
    return when (this) {
        ServiceResponse.DirectCallSetting.YES -> fr.outadoc.pavikt.minipavi.data.model.ServiceResponseDTO.DirectCallSetting.YES
        ServiceResponse.DirectCallSetting.NO -> fr.outadoc.pavikt.minipavi.data.model.ServiceResponseDTO.DirectCallSetting.NO
        ServiceResponse.DirectCallSetting.YES_CNX -> fr.outadoc.pavikt.minipavi.data.model.ServiceResponseDTO.DirectCallSetting.YES_CNX
    }
}

inline fun <reified T : Any> GatewayRequestDTO.mapToDomain(
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
                ?.let { context -> Json.decodeFromString(context) }
                ?: initialState,
            function = payload.function.mapToDomain()
        ),
        urlParams = urlParams
    )
}

fun GatewayRequestDTO.Function.mapToDomain(): GatewayRequest.Function {
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

fun GatewayRequestDTO.SocketType.mapToDomain(): GatewayRequest.SocketType {
    return when (this) {
        GatewayRequestDTO.SocketType.WebSocketSSL -> GatewayRequest.SocketType.WebSocketSSL
        GatewayRequestDTO.SocketType.WebSocket -> GatewayRequest.SocketType.WebSocket
        GatewayRequestDTO.SocketType.Asterisk -> GatewayRequest.SocketType.Asterisk
        GatewayRequestDTO.SocketType.Telnet -> GatewayRequest.SocketType.Telnet
    }
}
