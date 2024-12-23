package fr.outadoc.kpavi.api.server

import fr.outadoc.kpavi.api.data.minitelApp
import fr.outadoc.kpavi.api.domain.model.ServiceResponse
import fr.outadoc.kpavi.videotex.BackgroundColor
import fr.outadoc.kpavi.videotex.TextColor
import fr.outadoc.kpavi.videotex.buildVideotex
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface HelloWorldState {

    @Serializable
    @SerialName("p1")
    data object Page1 : HelloWorldState

    @Serializable
    @SerialName("p2")
    data object Page2 : HelloWorldState
}

fun Route.helloWorld() {
    minitelApp<HelloWorldState>("/") { request ->
        when (request.payload.context) {
            null, HelloWorldState.Page1 -> {
                ServiceResponse(
                    version = "0.1",
                    context = HelloWorldState.Page2,
                    content = buildVideotex {
                        clearScreen()
                        appendLine("Bonjour le monde !")
                        appendLine("Vous êtes ${request.payload.uniqueId}")
                    },
                )
            }

            HelloWorldState.Page2 -> {
                ServiceResponse(
                    version = "0.1",
                    context = HelloWorldState.Page1,
                    content = buildVideotex {
                        clearScreen()
                        TextColor.entries.forEach { color ->
                            withTextColor(color) {
                                appendLine("Texte en ${color.name}")
                            }
                        }

                        BackgroundColor.entries.forEach { color ->
                            withBackgroundColor(color) {
                                appendLine(" Fond en ${color.name}")
                            }
                        }
                    },
                )
            }
        }
    }
}
