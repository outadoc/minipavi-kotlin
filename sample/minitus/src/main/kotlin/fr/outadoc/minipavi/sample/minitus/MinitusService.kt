package fr.outadoc.minipavi.sample.minitus

import fr.outadoc.minipavi.core.ktor.minitelService
import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minipavi.videotex.CharacterSize
import fr.outadoc.minipavi.videotex.Color
import fr.outadoc.minipavi.videotex.VideotexBuilder
import fr.outadoc.minipavi.videotex.buildVideotex
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import kotlinx.io.readLineStrict
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface MinitusState {
    @Serializable
    @SerialName("intro")
    data object IntroPage : MinitusState
}

private object Constants {
    const val MAX_ATTEMPTS = 6

    // On ne veut que des mots en majuscules de 5 à 10 caractères
    val ALLOWED_WORD_REGEX = Regex("^[A-Z]{5,10}$")

    const val MAX_READ_LINE_LENGTH = 32L
    const val DICT_PATH = "/dict/fr/dict.txt"
}

fun Application.minitus() {
    val words: Set<String> = readWords(environment)

    minitelService<MinitusState>(
        path = "/",
        version = "0.1",
        initialState = MinitusState.IntroPage,
    ) { request ->
        when (request.state) {
            MinitusState.IntroPage -> {
                ServiceResponse(
                    state = MinitusState.IntroPage,
                    content =
                        buildVideotex {
                            clearAll()
                            displayLogo()
                        },
                )
            }
        }
    }
}

private fun VideotexBuilder.displayLogo() {
    moveCursorTo(14, 2)

    withCharacterSize(CharacterSize.DoubleSize) {
        append('M')
        withInvertedBackground {
            withTextColor(Color.Yellow) {
                append('I')
            }
        }
        append("NI")
        withInvertedBackground {
            withTextColor(Color.Red) {
                append('T')
            }
        }
        append('U')
        withInvertedBackground {
            withTextColor(Color.Yellow) {
                append('S')
            }
        }
        appendLine()
    }
}

private fun readWords(environment: ApplicationEnvironment): Set<String> =
    buildSet {
        readResource(Constants.DICT_PATH)
            .use { wordSource ->
                try {
                    while (!wordSource.exhausted()) {
                        val word =
                            wordSource
                                .readLineStrict(
                                    limit = Constants.MAX_READ_LINE_LENGTH,
                                )
                                .normalize()

                        if (word.matches(Constants.ALLOWED_WORD_REGEX)) {
                            add(word)
                            environment.log.debug("AJOUTÉ : $word")
                        } else {
                            environment.log.debug("IGNORÉ : $word")
                        }
                    }
                } catch (e: Exception) {
                    environment.log.error("Erreur lors de la lecture du dictionnaire", e)
                }
            }

        environment.log.info("Chargé $size mots avec succès")
    }
