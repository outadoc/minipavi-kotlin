package fr.outadoc.kpavi.api.server

import fr.outadoc.kpavi.api.data.model.ServiceResponse
import fr.outadoc.kpavi.videotex.BackgroundColor
import fr.outadoc.kpavi.videotex.TextColor
import fr.outadoc.kpavi.videotex.buildVideotex
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.helloWorld() {
    post("/") {
        val request = call.getPaviRequest<String>()
        call.respond(
            ServiceResponse(
                version = "0.1",
                content = buildVideotex {
                    clearScreen()
                    appendLine("Bonjour le monde !")
                    withTextColor(TextColor.RED) {
                        appendLine("Texte rouge")
                    }
                    withBackgroundColor(BackgroundColor.GREEN) {
                        appendLine("abcdefghijklmnopqrstuvwxyz")
                    }
                    withRouleau {
                        appendLine("Rouleau")
                    }
                    withBlink {
                        appendLine("Clignotant")
                    }
                },
                context = ""
            )
        )
    }
}
