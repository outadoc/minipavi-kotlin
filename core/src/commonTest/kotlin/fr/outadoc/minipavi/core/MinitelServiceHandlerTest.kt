package fr.outadoc.minipavi.core

import fr.outadoc.minipavi.core.model.FunctionKey
import fr.outadoc.minipavi.core.model.GatewayRequest
import fr.outadoc.minipavi.core.model.ServiceResponse
import kotlinx.io.bytestring.encodeToByteString
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class MinitelServiceHandlerTest {

    @Serializable
    data class Counter(val count: Int)

    private class RecordingLogger : MinitelLogger {
        val entries = mutableListOf<Pair<MinitelLogger.Level, String>>()

        override fun log(
            level: MinitelLogger.Level,
            throwable: Throwable?,
            message: () -> String,
        ) {
            entries += level to message()
        }
    }

    private fun requestBody(
        context: String,
        extraField: String = "",
    ): String {
        val escapedContext = context.replace("\"", "\\\"")
        //language=JSON
        return """
            {
              "PAVI": {
                "version": "1.2",
                "uniqueId": "1714813976258",
                "remoteAddr": "82.65.112.8",
                "typesocket": "WSS",
                "versionminitel": "Cv;",
                "content": ["HELLO"],
                "context": "$escapedContext",
                "fctn": "ENVOI"$extraField
              }
            }
        """.trimIndent()
    }

    private fun handler(
        logger: MinitelLogger = MinitelLogger.None,
        onRequest: (GatewayRequest<Counter>) -> Unit = {},
    ) = MinitelServiceHandler<Counter>(
        version = "0.1",
        initialState = { Counter(0) },
        logger = logger,
    ) { request ->
        onRequest(request)
        ServiceResponse(
            state = Counter(request.state.count + 1),
            content = "hello".encodeToByteString(),
        )
    }

    @Test
    fun aValidRequestIsDecodedAndTheResponseIsEncoded() {
        var received: GatewayRequest<Counter>? = null

        val response = handler(onRequest = { received = it }).handle(requestBody(context = """{"count":41}"""))

        assertEquals(Counter(41), received?.state)
        assertEquals(listOf("HELLO"), received?.userInput)
        assertEquals(GatewayRequest.Event.KeyboardInput(FunctionKey.Envoi), received?.event)
        assertTrue(response.contains(""""version":"0.1""""), response)
        assertTrue(response.contains(""""context":"{\"count\":42}""""), response)
        assertTrue(response.contains(""""content":"aGVsbG8=""""), response)
    }

    @Test
    fun anEmptyContextUsesTheInitialState() {
        var received: GatewayRequest<Counter>? = null

        handler(onRequest = { received = it }).handle(requestBody(context = ""))

        assertEquals(Counter(0), received?.state)
    }

    @Test
    fun aCorruptContextUsesTheInitialStateAndLogsAnError() {
        val logger = RecordingLogger()
        var received: GatewayRequest<Counter>? = null

        handler(logger = logger, onRequest = { received = it }).handle(requestBody(context = "not json"))

        assertEquals(Counter(0), received?.state)
        assertEquals(1, logger.entries.count { (level, _) -> level == MinitelLogger.Level.Error })
    }

    @Test
    fun unknownKeysAreIgnored() {
        var received: GatewayRequest<Counter>? = null

        handler(onRequest = { received = it })
            .handle(requestBody(context = "", extraField = ""","somethingNew": 42"""))

        assertEquals(Counter(0), received?.state)
    }

    @Test
    fun anInvalidBodyThrows() {
        assertFailsWith<InvalidGatewayRequestException> {
            handler().handle("""{"not":"a request"}""")
        }
        assertFailsWith<InvalidGatewayRequestException> {
            handler().handle("not even json")
        }
    }
}
