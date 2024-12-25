package fr.outadoc.kpavi.api.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GatewayRequestDTO(
    @SerialName("PAVI")
    val payload: Payload,

    @SerialName("URLPARAMS")
    val urlParams: List<String>? = null,
) {
    @Serializable
    data class Payload(
        @SerialName("version")
        val version: String,

        @SerialName("uniqueId")
        val uniqueId: String,

        @SerialName("remoteAddr")
        val remoteAddress: String,

        @SerialName("typesocket")
        val socketType: SocketType,

        @SerialName("versionminitel")
        val minitelVersion: String,

        @SerialName("content")
        val content: List<String>,

        @SerialName("context")
        val context: String,

        @SerialName("fctn")
        val function: Function
    )

    enum class SocketType {
        @SerialName("WS")
        WebSocket,

        @SerialName("WSS")
        WebSocketSSL,

        @SerialName("AST")
        Asterisk,

        @SerialName("TELN")
        Telnet,
    }

    enum class Function {
        @SerialName("ENVOI")
        ENVOI,

        @SerialName("SUITE")
        SUITE,

        @SerialName("RETOUR")
        RETOUR,

        @SerialName("ANNULATION")
        ANNULATION,

        @SerialName("CORRECTION")
        CORRECTION,

        @SerialName("GUIDE")
        GUIDE,

        @SerialName("REPETITION")
        REPETITION,

        @SerialName("SOMMAIRE")
        SOMMAIRE,

        @SerialName("CNX")
        CONNECTION,

        @SerialName("FIN")
        FIN,

        @SerialName("DIRECT")
        DIRECT,

        @SerialName("DIRECTCNX")
        DIRECT_CONNECTION,

        @SerialName("DIRECTCALLFAILED")
        DIRECT_CALL_FAILED,

        @SerialName("DIRECTCALLENDED")
        DIRECT_CALL_ENDED,

        @SerialName("BGCALL")
        BACKGROUND_CALL,

        @SerialName("BGCALL_SIMU")
        SIMULATED_BACKGROUND_CALL,
    }
}
