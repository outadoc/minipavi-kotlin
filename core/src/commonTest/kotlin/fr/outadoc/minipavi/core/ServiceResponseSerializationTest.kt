package fr.outadoc.minipavi.core

import fr.outadoc.minipavi.core.internal.data.model.ServiceResponseDTO
import kotlinx.io.bytestring.encodeToByteString
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ServiceResponseSerializationTest {

    @Test
    fun `Correctly encode InputMessage command`() {
        //language=JSON
        val expected = """
            {
              "version": "1.0",
              "context": "a:1:{s:3:\"url\";s:0:\"\";}",
              "content": "SGVsbG8gV29ybGQh",
              "echo": "on",
              "directcall": "no",
              "next": "http://www.monsite.fr/index.php?step=20",
              "COMMAND": {
                "name": "InputMsg",
                "param": {
                  "x": 1,
                  "y": 13,
                  "w": 40,
                  "h": 2,
                  "spacechar": ".",
                  "prefill": [],
                  "cursor": "on",
                  "validwith": 152
                }
              }
            }
        """.trimIndent()

        val payload = ServiceResponseDTO(
            version = "1.0",
            content = "Hello World!".encodeToByteString(),
            context = "a:1:{s:3:\"url\";s:0:\"\";}",
            echo = ServiceResponseDTO.Command.OnOff.ON,
            directCall = ServiceResponseDTO.DirectCallSetting.NO,
            nextUrl = "http://www.monsite.fr/index.php?step=20",
            command = ServiceResponseDTO.Command.InputMessage(
                params = ServiceResponseDTO.Command.InputMessage.Params(
                    x = 1,
                    y = 13,
                    width = 40,
                    height = 2,
                    spaceChar = ".",
                    prefill = emptyList(),
                    cursor = ServiceResponseDTO.Command.OnOff.ON,
                    submitWith = setOf(
                        ServiceResponseDTO.Command.FunctionKey.REPETITION,
                        ServiceResponseDTO.Command.FunctionKey.GUIDE,
                        ServiceResponseDTO.Command.FunctionKey.ENVOI
                    )
                )
            )
        )

        assertEquals(
            expected = expected,
            actual = json.encodeToString(payload)
        )
    }

    @Test
    fun `Correctly encode InputText command`() {
        //language=JSON
        val expected = """
            {
              "version": "1.0",
              "context": "a:0:{}",
              "content": "SGVsbG8gV29ybGQh",
              "echo": "on",
              "directcall": "no",
              "next": "http://www.monsite.fr/index.php?step=20",
              "COMMAND": {
                "name": "InputTxt",
                "param": {
                  "x": 2,
                  "y": 20,
                  "l": 10,
                  "char": "",
                  "spacechar": ".",
                  "prefill": "Salut!",
                  "cursor": "on",
                  "validwith": 136
                }
              }
            }
        """.trimIndent()

        val payload = ServiceResponseDTO(
            version = "1.0",
            content = "Hello World!".encodeToByteString(),
            context = "a:0:{}",
            echo = ServiceResponseDTO.Command.OnOff.ON,
            directCall = ServiceResponseDTO.DirectCallSetting.NO,
            nextUrl = "http://www.monsite.fr/index.php?step=20",
            command = ServiceResponseDTO.Command.InputText(
                params = ServiceResponseDTO.Command.InputText.Params(
                    x = 2,
                    y = 20,
                    length = 10,
                    char = "",
                    spaceChar = ".",
                    prefill = "Salut!",
                    cursor = ServiceResponseDTO.Command.OnOff.ON,
                    submitWith = setOf(
                        ServiceResponseDTO.Command.FunctionKey.REPETITION,
                        ServiceResponseDTO.Command.FunctionKey.ENVOI
                    )
                )
            )
        )

        assertEquals(
            expected = expected,
            actual = json.encodeToString(payload)
        )
    }

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        prettyPrint = true
        prettyPrintIndent = "  "
        encodeDefaults = true
    }
}