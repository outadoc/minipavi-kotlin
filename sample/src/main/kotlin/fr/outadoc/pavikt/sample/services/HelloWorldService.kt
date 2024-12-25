package fr.outadoc.pavikt.sample.services

import fr.outadoc.pavikt.minipavi.ktor.minitelService
import fr.outadoc.pavikt.minipavi.model.ServiceResponse
import fr.outadoc.pavikt.videotex.CharacterSize
import fr.outadoc.pavikt.videotex.TextColor
import fr.outadoc.pavikt.videotex.buildVideotex
import io.ktor.server.application.Application
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface HelloWorldState {
    @Serializable
    @SerialName("intro")
    data object IntroPage : HelloWorldState

    @Serializable
    @SerialName("textColor")
    data object TextColorPage : HelloWorldState

    @Serializable
    @SerialName("textFormat")
    data object TextFormatPage : HelloWorldState
}

/**
 * Un service qui affiche plusieurs pages de texte formaté et démontre la saisie de texte.
 */
fun Application.helloWorld() {
    minitelService<HelloWorldState>(
        path = "/",
        version = "0.1",
        initialState = HelloWorldState.IntroPage,
    ) { request ->
        when (request.payload.state) {
            HelloWorldState.IntroPage -> {
                ServiceResponse(
                    state = HelloWorldState.TextColorPage,
                    content =
                        buildVideotex {
                            clearAll()
                            appendLine()

                            withCharacterSize(CharacterSize.DOUBLE_HEIGHT) {
                                appendLine(" Bonjour le monde !")
                            }
                            appendLine()

                            append(" Vous êtes ")
                            withTextColor(TextColor.RED) {
                                appendLine(request.payload.sessionId)
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

            HelloWorldState.TextColorPage -> {
                ServiceResponse(
                    state = HelloWorldState.TextFormatPage,
                    command =
                        ServiceResponse.Command.InputForm(
                            x = listOf(1),
                            y = listOf(19),
                            length = listOf(30),
                        ),
                    content =
                        buildVideotex {
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

                            appendLine()
                            appendLine("Test de l'entrée utilisateur :")
                        },
                )
            }

            HelloWorldState.TextFormatPage -> {
                ServiceResponse(
                    state = HelloWorldState.IntroPage,
                    content =
                        buildVideotex {
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

                            appendLine()
                            appendLine("Entrée utilisateur : ")
                            request.payload.userInput.forEach { line ->
                                appendLine(line)
                            }
                        },
                )
            }
        }
    }
}
