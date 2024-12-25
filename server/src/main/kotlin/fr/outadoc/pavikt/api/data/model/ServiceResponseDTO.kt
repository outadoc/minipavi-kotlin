package fr.outadoc.pavikt.api.data.model

import fr.outadoc.pavikt.api.data.serializer.Base64Serializer
import fr.outadoc.pavikt.api.data.serializer.FunctionKeySetSerializer
import fr.outadoc.pavikt.api.data.serializer.InstantUnixEpochSerializer
import kotlinx.datetime.Instant
import kotlinx.io.bytestring.ByteString
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
data class ServiceResponseDTO(

    @SerialName("version")
    val version: String,

    @SerialName("context")
    val context: String,

    @SerialName("content")
    @Serializable(with = Base64Serializer::class)
    val content: ByteString,

    @SerialName("echo")
    val echo: CommandDTO.OnOff,

    @SerialName("directcall")
    val directCall: DirectCallSetting = DirectCallSetting.NO,

    @SerialName("next")
    val next: String,

    @SerialName("COMMAND")
    val command: CommandDTO?,
) {
    @Serializable
    enum class DirectCallSetting {
        @SerialName("no")
        NO,

        @SerialName("yes")
        YES,

        @SerialName("yes-cnx")
        YES_CNX
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("name")
sealed interface CommandDTO {

    @Serializable
    @SerialName("InputTxt")
    data class InputText(
        @SerialName("param")
        val params: Params,
    ) : CommandDTO {

        @Serializable
        data class Params(

            @SerialName("x")
            val x: Int,

            @SerialName("y")
            val y: Int,

            @SerialName("l")
            val length: Int,

            @SerialName("char")
            val char: String,

            @SerialName("spacechar")
            val spaceChar: String,

            @SerialName("prefill")
            val prefill: String,

            @SerialName("cursor")
            val cursor: OnOff,

            @SerialName("validwith")
            @Serializable(with = FunctionKeySetSerializer::class)
            val submitWith: Set<FunctionKey>,
        )
    }

    @Serializable
    @SerialName("InputMsg")
    data class InputMessage(
        @SerialName("param")
        val params: Params,
    ) : CommandDTO {

        @Serializable
        data class Params(

            @SerialName("x")
            val x: Int,

            @SerialName("y")
            val y: Int,

            @SerialName("w")
            val width: Int,

            @SerialName("h")
            val height: Int,

            @SerialName("spacechar")
            val spaceChar: String,

            @SerialName("prefill")
            val prefill: List<String>,

            @SerialName("cursor")
            val cursor: OnOff,

            @SerialName("validwith")
            @Serializable(with = FunctionKeySetSerializer::class)
            val submitWith: Set<FunctionKey>,
        )
    }

    @Serializable
    @SerialName("InputForm")
    data class InputForm(
        @SerialName("param")
        val params: Params,
    ) : CommandDTO {

        @Serializable
        data class Params(

            @SerialName("x")
            val x: List<Int>,

            @SerialName("y")
            val y: List<Int>,

            @SerialName("l")
            val length: List<Int>,

            @SerialName("spacechar")
            val spaceChar: String,

            @SerialName("prefill")
            val prefill: List<String>,

            @SerialName("cursor")
            val cursor: OnOff,

            @SerialName("validwith")
            @Serializable(with = FunctionKeySetSerializer::class)
            val submitWith: Set<FunctionKey>,
        )
    }

    @SerialName("libCnx")
    @Serializable
    data object Disconnect : CommandDTO

    @Serializable
    @SerialName("PushServiceMsg")
    data class PushServiceMessage(
        @SerialName("param")
        val params: Params,
    ) : CommandDTO {

        @Serializable
        data class Params(

            @SerialName("uniqueid")
            val uniqueIds: List<String>,

            @SerialName("message")
            val message: List<String>,
        )
    }

    @Serializable
    @SerialName("BackgroundCall")
    data class BackgroundCall(
        @SerialName("param")
        val params: Params
    ) : CommandDTO {

        @Serializable
        data class Params(

            @SerialName("time")
            @Serializable(with = InstantUnixEpochSerializer::class)
            val sendAt: Instant,

            @SerialName("simulate")
            val simulate: Boolean,

            @SerialName("uniqueid")
            val uniqueIds: List<String>,

            @SerialName("url")
            val url: String,
        )
    }

    @Serializable
    @SerialName("connectToWs")
    data class ConnectToWebSocket(
        @SerialName("param")
        val params: Params
    ) : CommandDTO {

        @Serializable
        data class Params(

            @SerialName("key")
            val key: String,

            @SerialName("host")
            val host: String,

            @SerialName("path")
            val path: String?,

            @SerialName("proto")
            val proto: String?,

            @SerialName("echo")
            val echo: OnOff,

            @SerialName("case")
            val case: Case,
        )
    }

    @Serializable
    @SerialName("connectToTln")
    data class ConnectToTelnet(
        @SerialName("param")
        val params: Params
    ) : CommandDTO {

        @Serializable
        data class Params(

            @SerialName("key")
            val key: String,

            @SerialName("host")
            val host: String,

            @SerialName("echo")
            val echo: OnOff,

            @SerialName("case")
            val case: Case,

            @SerialName("startseq")
            val startSequence: String,
        )
    }

    @Serializable
    @SerialName("connectToExt")
    data class ConnectToExt(
        @SerialName("param")
        val params: Params
    ) : CommandDTO {

        @Serializable
        data class Params(

            @SerialName("key")
            val key: String,

            @SerialName("number")
            val telNumber: String,

            @SerialName("RX")
            val rx: Int,

            @SerialName("TX")
            val tx: Int
        )
    }

    @Serializable
    @SerialName("duplicateStream")
    data class DuplicateStream(
        @SerialName("param")
        val params: Params
    ) : CommandDTO {

        @Serializable
        data class Params(

            @SerialName("key")
            val key: String,

            @SerialName("uniqueid")
            val uniqueId: String
        )
    }

    enum class OnOff {
        @SerialName("on")
        ON,

        @SerialName("off")
        OFF
    }

    enum class Case {
        @SerialName("upper")
        UPPER,

        @SerialName("lower")
        LOWER
    }

    enum class FunctionKey(val value: Int) {
        SOMMAIRE(1),
        RETOUR(4),
        REPETITION(8),
        GUIDE(16),
        SUITE(64),
        ENVOI(128)
    }
}
