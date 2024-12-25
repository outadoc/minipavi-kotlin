package fr.outadoc.pavikt.api.data

import fr.outadoc.pavikt.minipavi.internal.data.model.GatewayRequestDTO
import kotlinx.serialization.json.Json
import org.intellij.lang.annotations.Language
import kotlin.test.Test
import kotlin.test.assertEquals

class GatewayRequestDeserializationTest {

    @Test
    fun `When the user connects to the gateway via the web, then we correctly decode the request`() {
        @Language("JSON")
        val input = """
            {
              "PAVI": {
                "version": "1.2",
                "uniqueId": "1714813976258",
                "remoteAddr": "82.65.112.8",
                "typesocket": "WSS",
                "versionminitel": "Cv;",
                "content": [],
                "context": "",
                "fctn": "CNX"
              }
            }
        """.trimIndent()

        val expected = GatewayRequestDTO(
            payload = GatewayRequestDTO.Payload(
                version = "1.2",
                uniqueId = "1714813976258",
                remoteAddress = "82.65.112.8",
                socketType = GatewayRequestDTO.SocketType.WebSocketSSL,
                minitelVersion = "Cv;",
                content = emptyList(),
                context = "",
                function = GatewayRequestDTO.Function.CONNECTION,
            )
        )

        assertEquals(
            expected = expected,
            actual = Json.decodeFromString(input)
        )
    }

    @Test
    fun `When the user connects to the gateway via a phone line, then we correctly decode the request`() {
        @Language("JSON")
        val input = """
            {
              "PAVI": {
                "version": "1.2",
                "uniqueId": "1714813977354",
                "remoteAddr": "0184257626",
                "typesocket": "WS",
                "versionminitel": "Cz6",
                "content": [],
                "context": "",
                "fctn": "CNX"
              }
            }
        """.trimIndent()

        val expected = GatewayRequestDTO(
            payload = GatewayRequestDTO.Payload(
                version = "1.2",
                uniqueId = "1714813977354",
                remoteAddress = "0184257626",
                socketType = GatewayRequestDTO.SocketType.WebSocket,
                minitelVersion = "Cz6",
                content = emptyList(),
                context = "",
                function = GatewayRequestDTO.Function.CONNECTION,
            )
        )

        assertEquals(
            expected = expected,
            actual = Json.decodeFromString(input)
        )
    }

    @Test
    fun `When the user sends some text and presses ENVOI, then we correctly decode the request`() {
        @Language("JSON")
        val input = """
            {
              "PAVI": {
                "version": "1.2",
                "uniqueId": "1714813977354",
                "remoteAddr": "0184257626",
                "typesocket": "WS",
                "versionminitel": "Cz6",
                "content": [
                  "ligne 1",
                  "ligne 2"
                ],
                "context": "",
                "fctn": "ENVOI"
              }
            }
        """.trimIndent()

        val expected = GatewayRequestDTO(
            payload = GatewayRequestDTO.Payload(
                version = "1.2",
                uniqueId = "1714813977354",
                remoteAddress = "0184257626",
                socketType = GatewayRequestDTO.SocketType.WebSocket,
                minitelVersion = "Cz6",
                content = listOf("ligne 1", "ligne 2"),
                context = "",
                function = GatewayRequestDTO.Function.ENVOI,
            )
        )

        assertEquals(
            expected = expected,
            actual = Json.decodeFromString(input)
        )
    }
}