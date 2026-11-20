package fr.outadoc.minipavi.core.ktor

import fr.outadoc.minipavi.core.model.ServiceResponse
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import kotlinx.io.bytestring.encodeToByteString
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MinitelServiceTest {

    @Serializable
    data class Counter(val count: Int)

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

    private fun io.ktor.server.application.Application.counterService(path: String) {
        minitelService<Counter>(
            path = path,
            version = "0.1",
            initialState = { Counter(0) },
        ) { request ->
            ServiceResponse(
                state = Counter(request.state.count + 1),
                content = "hello".encodeToByteString(),
            )
        }
    }

    @Test
    fun postingAValidRequestReturnsTheResponse() = testApplication {
        application { counterService("/") }

        val response = client.post("/") { setBody(validRequest) }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.Application.Json, response.contentType()?.withoutParameters())
        val body = response.bodyAsText()
        assertTrue(body.contains("\"context\":\"{\\\"count\\\":42}\""), body)
        assertTrue(body.contains("\"content\":\"aGVsbG8=\""), body)
    }

    @Test
    fun postingToAnotherPathReturnsNotFound() = testApplication {
        application { counterService("/service") }

        val response = client.post("/other") { setBody(validRequest) }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun postingAnInvalidBodyReturnsBadRequest() = testApplication {
        application { counterService("/") }

        val response = client.post("/") { setBody("{\"not\":\"a request\"}") }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("Bad Request", response.bodyAsText())
    }

    @Test
    fun twoServicesCanLiveInTheSameApplication() = testApplication {
        application {
            counterService("/a")
            counterService("/b")
        }

        assertEquals(HttpStatusCode.OK, client.post("/a") { setBody(validRequest) }.status)
        assertEquals(HttpStatusCode.OK, client.post("/b") { setBody(validRequest) }.status)
    }
}
