package fr.outadoc.minipavi.core.cloudflare

import fr.outadoc.minipavi.core.MinitelServiceHandler
import fr.outadoc.minipavi.core.model.ServiceResponse
import kotlinx.io.bytestring.encodeToByteString
import kotlinx.serialization.Serializable
import kotlin.js.Promise
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FetchHandlerTest {

    @Serializable
    data class Counter(val count: Int)

    private val fetch =
        MinitelServiceHandler<Counter>(
            version = "0.1",
            initialState = { Counter(0) },
        ) { request ->
            ServiceResponse(
                state = Counter(request.state.count + 1),
                content = "hello".encodeToByteString(),
            )
        }.toFetchHandler(path = "/service")

    //language=JSON
    private val validRequest = """
        {
          "PAVI": {
            "version": "1.2",
            "uniqueId": "1714813976258",
            "remoteAddr": "82.65.112.8",
            "typesocket": "WSS",
            "versionminitel": "Cv;",
            "content": [],
            "context": "{\"count\":41}",
            "fctn": "ENVOI"
          }
        }
    """.trimIndent()

    private fun request(
        method: String,
        url: String,
        body: String? = null,
    ): Request {
        val init = js("{}").unsafeCast<RequestInit>()
        init.method = method
        if (body != null) init.body = body
        return Request(url, init)
    }

    @Test
    fun postingAValidRequestReturnsTheResponse(): Promise<*> =
        fetch(request("POST", "https://example.com/service", validRequest)).then { response ->
            assertEquals(200, response.status)
            assertTrue(response.headers.get("content-type").orEmpty().startsWith("application/json"))
            response.text().then { body ->
                assertTrue(body.contains("\"context\":\"{\\\"count\\\":42}\""), body)
                assertTrue(body.contains("\"content\":\"aGVsbG8=\""), body)
            }
        }

    @Test
    fun anotherMethodReturnsMethodNotAllowed(): Promise<*> =
        fetch(request("GET", "https://example.com/service")).then { response ->
            assertEquals(405, response.status)
            assertEquals("POST", response.headers.get("allow"))
        }

    @Test
    fun anotherPathReturnsNotFound(): Promise<*> =
        fetch(request("POST", "https://example.com/other", validRequest)).then { response ->
            assertEquals(404, response.status)
        }

    @Test
    fun anInvalidBodyReturnsBadRequest(): Promise<*> =
        fetch(request("POST", "https://example.com/service", "{\"not\":\"a request\"}")).then { response ->
            assertEquals(400, response.status)
            response.text().then { body -> assertEquals("Bad Request", body) }
        }
}
