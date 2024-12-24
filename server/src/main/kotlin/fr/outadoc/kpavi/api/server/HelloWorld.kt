package fr.outadoc.kpavi.api.server

import fr.outadoc.kpavi.api.data.minitelApp
import fr.outadoc.kpavi.api.domain.model.ServiceResponse
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
    minitelApp<HelloWorldState>(
        version = "0.1",
        initialState = HelloWorldState.Intro,
    ) { request ->
        when (request.payload.state) {
            HelloWorldState.Intro -> {
                ServiceResponse(
                    state = HelloWorldState.Text,
                    content = buildVideotex {
                        clearScreen()
                        appendLine("Bonjour le monde !")
                        appendLine("Vous êtes ${request.payload.uniqueId}")
                    },
                )
            }

            HelloWorldState.Text -> {
                ServiceResponse(
                    state = HelloWorldState.Background,
                    content = buildVideotex {
                        clearScreen()

                        TextColor.entries.forEach { color ->
                            withTextColor(color) {
                                appendLine("Texte en ${color.name}")
                            }
                        }

                        withInvertedBackground {
                            TextColor.entries.forEach { color ->
                                withTextColor(color) {
                                    appendLine("Texte en ${color.name}")
                                }
                            }
                        }
                    },
                )
            }

            HelloWorldState.Background -> {
                ServiceResponse(
                    state = HelloWorldState.Intro,
                    content = buildVideotex {
                        clearScreen()

                        withBlink {
                            appendLine("Texte clignotant")
                        }

                        withUnderline {
                            appendLine("Texte souligné")
                        }
                    },
                )
            }
        }
    }
}
