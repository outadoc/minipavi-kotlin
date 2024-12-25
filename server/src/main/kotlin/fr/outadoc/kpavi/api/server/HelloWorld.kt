package fr.outadoc.kpavi.api.server

import fr.outadoc.kpavi.api.data.minitelApp
import fr.outadoc.kpavi.api.domain.model.ServiceResponse
import fr.outadoc.kpavi.videotex.CharacterSize
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
                        clearAll()
                        appendLine()

                        withCharacterSize(CharacterSize.DOUBLE_HEIGHT) {
                            appendLine(" Bonjour le monde !")
                        }
                        appendLine()

                        append(" Vous êtes ")
                        withTextColor(TextColor.RED) {
                            appendLine(request.payload.uniqueId)
                        }

                        append(" ou ")
                        withTextColor(TextColor.GREEN) {
                            appendLine(request.payload.remoteAddress)
                        }

                        append(" Connecté.e via ")
                        withTextColor(TextColor.BLUE) {
                            appendLine(request.payload.socketType.toString())
                        }

                        append(" À l'aide d'un Minitel ")
                        withTextColor(TextColor.YELLOW) {
                            appendLine(request.payload.minitelVersion)
                        }
                    },
                )
            }

            HelloWorldState.Text -> {
                ServiceResponse(
                    state = HelloWorldState.Background,
                    content = buildVideotex {
                        clearAll()

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
                        clearAll()

                        withBlink {
                            appendLine(" Texte clignotant")
                        }

                        withUnderline {
                            appendLine(" Texte souligné")
                        }

                        appendLine()

                        withCharacterSize(CharacterSize.DOUBLE_HEIGHT) {
                            appendLine(" Tall boi")
                        }

                        withCharacterSize(CharacterSize.DOUBLE_WIDTH) {
                            appendLine(" Long boi")
                        }

                        appendLine()

                        withCharacterSize(CharacterSize.DOUBLE_SIZE) {
                            appendLine(" Big boi")
                        }
                    },
                )
            }
        }
    }
}
