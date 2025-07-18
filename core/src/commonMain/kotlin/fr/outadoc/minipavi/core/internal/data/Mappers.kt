package fr.outadoc.minipavi.core.internal.data

import fr.outadoc.minipavi.core.internal.KJson
import fr.outadoc.minipavi.core.internal.data.model.GatewayRequestDTO
import fr.outadoc.minipavi.core.internal.data.model.ServiceResponseDTO
import fr.outadoc.minipavi.core.model.GatewayRequest
import fr.outadoc.minipavi.core.model.FunctionKey
import fr.outadoc.minipavi.core.model.ServiceResponse
import kotlinx.serialization.KSerializer

internal fun <TState : Any> ServiceResponse<TState>.mapToDTO(
    stateSerializer: KSerializer<TState>,
    version: String,
): ServiceResponseDTO {
    return ServiceResponseDTO(
        version = version,
        content = content,
        context = KJson.encodeToString(stateSerializer, state),
        echo = echo.mapToDTO(),
        directCall = directCall.mapToDTO(),
        nextUrl = nextUrl,
        command = command.mapToDTO()
    )
}

internal fun ServiceResponse.Command.mapToDTO(): ServiceResponseDTO.Command? {
    return when (this) {
        is ServiceResponse.Command.Display -> {
            null
        }

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
                    uniqueId = userId
                )
            )
        }

        is ServiceResponse.Command.InputForm -> {
            ServiceResponseDTO.Command.InputForm(
                params = ServiceResponseDTO.Command.InputForm.Params(
                    x = cols,
                    y = lines,
                    length = length,
                    spaceChar = spaceChar.toString(),
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
                    uniqueIds = userIds,
                    message = message
                )
            )
        }
    }
}

internal fun FunctionKey.mapToDTO(): ServiceResponseDTO.Command.FunctionKey {
    return when (this) {
        FunctionKey.Sommaire -> ServiceResponseDTO.Command.FunctionKey.SOMMAIRE
        FunctionKey.Retour -> ServiceResponseDTO.Command.FunctionKey.RETOUR
        FunctionKey.Repetition -> ServiceResponseDTO.Command.FunctionKey.REPETITION
        FunctionKey.Guide -> ServiceResponseDTO.Command.FunctionKey.GUIDE
        FunctionKey.Suite -> ServiceResponseDTO.Command.FunctionKey.SUITE
        FunctionKey.Envoi -> ServiceResponseDTO.Command.FunctionKey.ENVOI
        FunctionKey.Annulation,
        FunctionKey.Correction,
            -> {
            error("Unsupported function key: $this")
        }
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

internal fun GatewayRequestDTO.Function.mapToDomain(): GatewayRequest.Event {
    return when (this) {
        GatewayRequestDTO.Function.ENVOI -> GatewayRequest.Event.KeyboardInput(FunctionKey.Envoi)
        GatewayRequestDTO.Function.SUITE -> GatewayRequest.Event.KeyboardInput(FunctionKey.Suite)
        GatewayRequestDTO.Function.RETOUR -> GatewayRequest.Event.KeyboardInput(FunctionKey.Retour)
        GatewayRequestDTO.Function.ANNULATION -> GatewayRequest.Event.KeyboardInput(FunctionKey.Annulation)
        GatewayRequestDTO.Function.CORRECTION -> GatewayRequest.Event.KeyboardInput(FunctionKey.Correction)
        GatewayRequestDTO.Function.GUIDE -> GatewayRequest.Event.KeyboardInput(FunctionKey.Guide)
        GatewayRequestDTO.Function.REPETITION -> GatewayRequest.Event.KeyboardInput(FunctionKey.Repetition)
        GatewayRequestDTO.Function.SOMMAIRE -> GatewayRequest.Event.KeyboardInput(FunctionKey.Sommaire)
        GatewayRequestDTO.Function.CONNECTION -> GatewayRequest.Event.Connection
        GatewayRequestDTO.Function.FIN -> GatewayRequest.Event.Disconnection
        GatewayRequestDTO.Function.DIRECT -> GatewayRequest.Event.Direct
        GatewayRequestDTO.Function.DIRECT_CONNECTION -> GatewayRequest.Event.DirectConnection
        GatewayRequestDTO.Function.DIRECT_CALL_FAILED -> GatewayRequest.Event.DirectCallFailed
        GatewayRequestDTO.Function.DIRECT_CALL_ENDED -> GatewayRequest.Event.DirectCallEnded
        GatewayRequestDTO.Function.BACKGROUND_CALL -> GatewayRequest.Event.BackgroundCall
        GatewayRequestDTO.Function.SIMULATED_BACKGROUND_CALL -> GatewayRequest.Event.BackgroundCallSimulated
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
