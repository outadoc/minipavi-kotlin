package fr.outadoc.kpavi.api.data

import fr.outadoc.kpavi.api.data.model.Command
import fr.outadoc.kpavi.api.data.model.GatewayRequest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.intellij.lang.annotations.Language
import kotlin.test.Test
import kotlin.test.assertEquals

class GatewayRequestTest {

    @Test
    fun `When the user connects to the gateway via the web, then we correctly decode the request`() {
        @Language("JSON")
        val input = """
            {
              "PAVI": {
                "version": "1.2",
                "uniqueId": "1714813976258",
                "remoteAddr": "82.65.112.8",
                "typesocket": "websocketssl",
                "versionminitel": "Cv;",
                "content": [],
                "context": "",
                "fctn": "CNX"
              }
            }
        """.trimIndent()

        val expected = GatewayRequest(
            payload = GatewayRequest.Payload(
                version = "1.2",
                uniqueId = "1714813976258",
                remoteAddress = "82.65.112.8",
                socketType = GatewayRequest.SocketType.WebSocketSSL,
                minitelVersion = "Cv;",
                content = emptyList(),
                context = "",
                function = GatewayRequest.Function.CNX,
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
                "typesocket": "other",
                "versionminitel": "Cz6",
                "content": [],
                "context": "",
                "fctn": "CNX"
              }
            }
        """.trimIndent()

        val expected = GatewayRequest(
            payload = GatewayRequest.Payload(
                version = "1.2",
                uniqueId = "1714813977354",
                remoteAddress = "0184257626",
                socketType = GatewayRequest.SocketType.Other,
                minitelVersion = "Cz6",
                content = emptyList(),
                context = "",
                function = GatewayRequest.Function.CNX,
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
                "typesocket": "other",
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

        val expected = GatewayRequest(
            payload = GatewayRequest.Payload(
                version = "1.2",
                uniqueId = "1714813977354",
                remoteAddress = "0184257626",
                socketType = GatewayRequest.SocketType.Other,
                minitelVersion = "Cz6",
                content = listOf("ligne 1", "ligne 2"),
                context = "",
                function = GatewayRequest.Function.ENVOI,
            )
        )

        assertEquals(
            expected = expected,
            actual = Json.decodeFromString(input)
        )
    }
}