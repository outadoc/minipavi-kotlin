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
    @SerialName("intro")
    data object Intro : HelloWorldState

    @Serializable
    @SerialName("text")
    data object Text : HelloWorldState

    @Serializable
    @SerialName("background")
    data object Background : HelloWorldState
}

fun Route.helloWorld() {
    minitelApp<HelloWorldState>(version = "0.1") { request ->
        when (request.payload.context) {
            null, HelloWorldState.Intro -> {
                ServiceResponse(
                    context = HelloWorldState.Text,
                    content = buildVideotex {
                        clearScreen()
                        appendLine("Bonjour le monde !")
                        appendLine("Vous êtes ${request.payload.uniqueId}")
                    },
                )
            }

            HelloWorldState.Text -> {
                ServiceResponse(
                    context = HelloWorldState.Background,
                    content = buildVideotex {
                        clearScreen()
                        TextColor.entries.forEach { color ->
                            withTextColor(color) {
                                appendLine("Texte en ${color.name}")
                            }
                        }
                    },
                )
            }

            HelloWorldState.Background -> {
                ServiceResponse(
                    context = HelloWorldState.Intro,
                    content = buildVideotex {
                        clearScreen()
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
