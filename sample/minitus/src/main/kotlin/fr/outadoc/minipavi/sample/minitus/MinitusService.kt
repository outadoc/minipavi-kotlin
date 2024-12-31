package fr.outadoc.minipavi.sample.minitus

import fr.outadoc.minipavi.core.ktor.minitelService
import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minipavi.videotex.CharacterSize
import fr.outadoc.minipavi.videotex.Color
import fr.outadoc.minipavi.videotex.VideotexBuilder
import fr.outadoc.minipavi.videotex.buildVideotex
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.readLineStrict
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface MinitusState {

    val date: LocalDate
    val guesses: List<String>

    @Serializable
    @SerialName("intro")
    data class Playing(
        override val date: LocalDate,
        override val guesses: List<String>,
        val lastInputWasInvalid: Boolean = false,
    ) : MinitusState

    @Serializable
    @SerialName("win")
    data class Win(
        override val date: LocalDate,
        override val guesses: List<String>,
    ) : MinitusState
}

private object Constants {
    const val MAX_ATTEMPTS = 6

    // On ne veut que des mots en majuscules de 5 à 10 caractères
    val ALLOWED_WORD_REGEX = Regex("^[A-Z]{5,10}$")

    const val MAX_READ_LINE_LENGTH = 32L
    const val DICT_PATH = "/dict/fr/dict.txt"
}

fun Application.minitus() {
    val clock = Clock.System
    val tz = TimeZone.of("Europe/Paris")

    val dictionary: Set<String> = readWords(environment)

    minitelService<MinitusState>(
        path = "/",
        version = "0.1",
        initialState = MinitusState.Playing(
            guesses = emptyList(),
            date = clock.now().toLocalDateTime(tz).date,
        ),
    ) { request ->
        val nextState = request.state.reduce(
            date = request.state.date,
            userInput = request.userInput.firstOrNull() ?: "",
            dictionary = dictionary,
        )

        ServiceResponse(
            state = nextState,
            content =
                buildVideotex {
                    clearAll()
                    displayLogo()

                    showGameState(nextState)
                },
        )
    }
}

private fun MinitusState.reduce(
    date: LocalDate,
    userInput: String,
    dictionary: Set<String>,
): MinitusState {
    return when (this) {
        is MinitusState.Playing -> reduce(date, userInput, dictionary)
        is MinitusState.Win -> this
    }
}

private fun MinitusState.Playing.reduce(
    date: LocalDate,
    userInput: String,
    dictionary: Set<String>,
): MinitusState {
    val inputWord = userInput.normalize()
    if (inputWord.isEmpty()) {
        return this
    }

    if (inputWord !in dictionary) {
        return copy(
            lastInputWasInvalid = true,
        )
    }

    val expectedWord = dictionary.pickDailyWord(date)

    if (inputWord == expectedWord) {
        return MinitusState.Win(
            date = date,
            guesses = guesses + inputWord,
        )
    }

    return copy(
        guesses = guesses + inputWord,
        lastInputWasInvalid = false,
    )
}

private fun VideotexBuilder.showGameState(state: MinitusState) {
    if (state is MinitusState.Playing && state.lastInputWasInvalid) {
        withTextColor(Color.Red) {
            appendLine("Désolé, ce mot n'est pas dans mon dictionnaire.")
        }
    }

    if (state is MinitusState.Win) {
        withTextColor(Color.Green) {
            appendLine("Bravo, vous avez trouvé le mot du jour !")
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
