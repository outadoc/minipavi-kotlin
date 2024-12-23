package fr.outadoc.kpavi.api.server

import fr.outadoc.kpavi.api.data.model.ServiceResponse
import fr.outadoc.kpavi.videotex.BackgroundColor
import fr.outadoc.kpavi.videotex.TextColor
import fr.outadoc.kpavi.videotex.buildVideotex
import io.ktor.server.routing.*

fun Route.helloWorld() {
    miniRoute("/") { request ->
        ServiceResponse(
            version = "0.1",
            content = buildVideotex {
                clearScreen()
                appendLine("Bonjour le monde !")
                appendLine("Vous etes ${request.payload.uniqueId}")

                TextColor.entries.forEach { color ->
                    withTextColor(color) {
                        appendLine("Texte en ${color.name}")
                    }
                }

                BackgroundColor.entries.forEach { color ->
                    withBackgroundColor(color) {
                        appendLine("Fond en ${color.name}")
                    }
                }
            },
            context = ""
        )
    }
}
