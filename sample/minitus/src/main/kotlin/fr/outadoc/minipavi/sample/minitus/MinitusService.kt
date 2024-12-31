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
import java.lang.Character.UnicodeBlock
import java.text.Normalizer

@Serializable
sealed interface MinitusState {
    @Serializable
    @SerialName("intro")
    data object IntroPage : MinitusState
}

private object Constants {
    const val MaxAttempts = 6

    // On ne veut que des mots en majuscules de 5 à 10 caractères
    val AllowedWordRegex = Regex("^[A-Z]{5,10}$")

    const val MaxReadLineLength = 32L
    val DictPath = "/dict/fr/dict.txt"
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
        readResource(Constants.DictPath)
            .use { wordSource ->
                try {
                    while (!wordSource.exhausted()) {
                        val word = wordSource
                            .readLineStrict(
                                limit = Constants.MaxReadLineLength
                            )
                            .normalize()

                        if (word.matches(Constants.AllowedWordRegex)) {
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

private fun String.normalize(): String {
    // On normalise la chaîne de caractères en NFD,
    // c'est-à-dire en décomposant les caractères en base et diacritiques
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .filter { char ->
            // On supprime toutes les diacritiques
            UnicodeBlock.of(char) != UnicodeBlock.COMBINING_DIACRITICAL_MARKS
        }
        .uppercase()
}
