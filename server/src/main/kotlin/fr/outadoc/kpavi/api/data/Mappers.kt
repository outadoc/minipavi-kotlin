package fr.outadoc.kpavi.api.data

import fr.outadoc.kpavi.api.data.model.CommandDTO
import fr.outadoc.kpavi.api.data.model.GatewayRequestDTO
import fr.outadoc.kpavi.api.data.model.ServiceResponseDTO
import fr.outadoc.kpavi.api.domain.model.Command
import fr.outadoc.kpavi.api.domain.model.GatewayRequest
import fr.outadoc.kpavi.api.domain.model.ServiceResponse
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
            CommandDTO.BackgroundCall(
                params = CommandDTO.BackgroundCall.Params(
                    sendAt = params.sendAt,
                    url = params.url,
                    simulate = params.simulate,
                    uniqueIds = params.uniqueIds
                )
            )
        }

        is Command.ConnectToExt -> {
            CommandDTO.ConnectToExt(
                params = CommandDTO.ConnectToExt.Params(
                    key = params.key,
                    telNumber = params.telNumber,
                    rx = params.rx,
                    tx = params.tx
                )
            )
        }

        is Command.ConnectToTelnet -> {
            CommandDTO.ConnectToTelnet(
                params = CommandDTO.ConnectToTelnet.Params(
                    host = params.host,
                    key = params.key,
                    echo = params.echo.mapToDTO(),
                    case = params.case.mapToDTO(),
                    startSequence = params.startSequence,
                )
            )
        }

        is Command.ConnectToWebSocket -> {
            CommandDTO.ConnectToWebSocket(
                params = CommandDTO.ConnectToWebSocket.Params(
                    key = params.key,
                    echo = params.echo.mapToDTO(),
                    case = params.case.mapToDTO(),
                    host = params.host,
                    path = params.path,
                    proto = params.proto,
                )
            )
        }

        is Command.Disconnect -> {
            CommandDTO.Disconnect
        }

        is Command.DuplicateStream -> {
            CommandDTO.DuplicateStream(
                params = CommandDTO.DuplicateStream.Params(
                    key = params.key,
                    uniqueId = params.uniqueId
                )
            )
        }

        is Command.InputForm -> {
            CommandDTO.InputForm(
                params = CommandDTO.InputForm.Params(
                    x = params.x,
                    y = params.y,
                    length = params.length,
                    spaceChar = params.spaceChar,
                    prefill = params.prefill,
                    cursor = params.cursor.mapToDTO(),
                    submitWith = params.submitWith.map { it.mapToDTO() }.toSet()
                )
            )
        }

        is Command.InputMessage -> {
            CommandDTO.InputMessage(
                params = CommandDTO.InputMessage.Params(
                    x = params.x,
                    y = params.y,
                    width = params.width,
                    height = params.height,
                    spaceChar = params.spaceChar,
                    prefill = params.prefill,
                    cursor = params.cursor.mapToDTO(),
                    submitWith = params.submitWith.map { it.mapToDTO() }.toSet()
                )
            )
        }

        is Command.InputText -> {
            CommandDTO.InputText(
                params = CommandDTO.InputText.Params(
                    x = params.x,
                    y = params.y,
                    length = params.length,
                    char = params.char,
                    spaceChar = params.spaceChar,
                    prefill = params.prefill,
                    cursor = params.cursor.mapToDTO(),
                    submitWith = params.submitWith.map { it.mapToDTO() }.toSet()
                )
            )
        }

        is Command.PushServiceMessage -> {
            CommandDTO.PushServiceMessage(
                params = CommandDTO.PushServiceMessage.Params(
                    uniqueIds = params.uniqueIds,
                    message = params.message
                )
            )
        }
    }
}

fun Command.FunctionKey.mapToDTO(): CommandDTO.FunctionKey {
    return when (this) {
        Command.FunctionKey.SOMMAIRE -> CommandDTO.FunctionKey.SOMMAIRE
        Command.FunctionKey.RETOUR -> CommandDTO.FunctionKey.RETOUR
        Command.FunctionKey.REPETITION -> CommandDTO.FunctionKey.REPETITION
        Command.FunctionKey.GUIDE -> CommandDTO.FunctionKey.GUIDE
        Command.FunctionKey.SUITE -> CommandDTO.FunctionKey.SUITE
        Command.FunctionKey.ENVOI -> CommandDTO.FunctionKey.ENVOI
    }
}

fun Command.Case.mapToDTO(): CommandDTO.Case {
    return when (this) {
        Command.Case.LOWER -> CommandDTO.Case.LOWER
        Command.Case.UPPER -> CommandDTO.Case.UPPER
    }
}

fun Command.OnOff.mapToDTO(): CommandDTO.OnOff {
    return when (this) {
        Command.OnOff.ON -> CommandDTO.OnOff.ON
        Command.OnOff.OFF -> CommandDTO.OnOff.OFF
    }
}

fun ServiceResponse.DirectCallSetting.mapToDTO(): ServiceResponseDTO.DirectCallSetting {
    return when (this) {
        ServiceResponse.DirectCallSetting.YES -> ServiceResponseDTO.DirectCallSetting.YES
        ServiceResponse.DirectCallSetting.NO -> ServiceResponseDTO.DirectCallSetting.NO
        ServiceResponse.DirectCallSetting.YES_CNX -> ServiceResponseDTO.DirectCallSetting.YES_CNX
    }
}

inline fun <reified T : Any> GatewayRequestDTO.mapToDomain(
    initialState: T
): GatewayRequest<T> {
    return GatewayRequest(
        payload = GatewayRequest.Payload(
            version = payload.version,
            uniqueId = payload.uniqueId,
            remoteAddress = payload.remoteAddress,
            socketType = payload.socketType.mapToDomain(),
            minitelVersion = payload.minitelVersion,
            content = payload.content,
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
