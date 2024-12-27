package fr.outadoc.minipavi.sample.services

import fr.outadoc.minipavi.core.ktor.minitelService
import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minipavi.videotex.CharacterSize
import fr.outadoc.minipavi.videotex.Color
import fr.outadoc.minipavi.videotex.buildVideotex
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

                            withCharacterSize(CharacterSize.DoubleHeight) {
                                appendLine(" Bonjour le monde !")
                            }
                            appendLine()

                            append(" Vous êtes ")
                            withTextColor(Color.Red) {
                                appendLine(request.payload.sessionId)
                            }

                            append(" ou ")
                            withTextColor(Color.Green) {
                                appendLine(request.payload.remoteAddress)
                            }

                            append(" Connecté.e via ")
                            withTextColor(Color.Blue) {
                                appendLine(request.payload.socketType.toString())
                            }

                            append(" À l'aide d'un Minitel ")
                            withTextColor(Color.Yellow) {
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
                            cols = listOf(1),
                            lines = listOf(19),
                            length = listOf(30),
                        ),
                    content =
                        buildVideotex {
                            clearAll()

                            Color.entries.forEach { color ->
                                withTextColor(color) {
                                    appendLine("Texte en ${color.name}")
                                }
                            }

                            appendLine()

                            Color.entries.forEach { color ->
                                withBackgroundColor(color) {
                                    appendLine("Texte sur ${color.name}")
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

                            withCharacterSize(CharacterSize.DoubleHeight) {
                                appendLine(" Tall boi")
                            }

                            withCharacterSize(CharacterSize.DoubleWidth) {
                                appendLine(" Long boi")
                            }

                            appendLine()

                            withCharacterSize(CharacterSize.DoubleSize) {
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
