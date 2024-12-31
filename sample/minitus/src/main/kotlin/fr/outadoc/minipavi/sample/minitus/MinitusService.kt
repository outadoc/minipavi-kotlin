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
        val lastInputError: Error? = null,
    ) : MinitusState

    @Serializable
    @SerialName("win")
    data class Win(
        override val date: LocalDate,
        override val guesses: List<String>,
    ) : MinitusState

    @Serializable
    @SerialName("lose")
    data class Lose(
        override val date: LocalDate,
        override val guesses: List<String>,
    ) : MinitusState

    @Serializable
    sealed interface Error {
        @Serializable
        data object NotInDictionary : Error

        @Serializable
        data object InvalidLength : Error
    }
}

private object Constants {
    const val MAX_ATTEMPTS = 6

    // On ne veut que des mots en majuscules de 5 à 10 caractères
    val ALLOWED_WORD_LENGTHS = 5..10
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

        val expectedWord = dictionary.pickDailyWord(nextState.date)

        ServiceResponse(
            state = nextState,
            content = when (nextState) {
                is MinitusState.Playing -> {
                    playingScreen(
                        state = nextState,
                        expectedWord = expectedWord,
                    )
                }

                is MinitusState.Lose -> {
                    loseScreen(
                        state = nextState,
                        expectedWord = expectedWord,
                    )
                }

                is MinitusState.Win -> {
                    winScreen(
                        state = nextState,
                        expectedWord = expectedWord,
                    )
                }
            },
            command = when (nextState) {
                is MinitusState.Playing -> {
                    ServiceResponse.Command.InputText(
                        col = 23,
                        line = 24,
                        length = expectedWord.length,
                    )
                }

                is MinitusState.Win,
                is MinitusState.Lose -> {
                    null
                }
            }
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
        is MinitusState.Lose -> this
    }
}

private fun MinitusState.Playing.reduce(
    date: LocalDate,
    userInput: String,
    dictionary: Set<String>,
): MinitusState {
    val inputWord = userInput.normalize()
    if (inputWord.isEmpty()) {
        return copy(
            lastInputError = null,
        )
    }

    if (inputWord.length !in Constants.ALLOWED_WORD_LENGTHS) {
        return copy(
            lastInputError = MinitusState.Error.InvalidLength,
        )
    }

    if (inputWord !in dictionary) {
        return copy(
            lastInputError = MinitusState.Error.NotInDictionary,
        )
    }

    val expectedWord = dictionary.pickDailyWord(date)

    if (inputWord == expectedWord) {
        return MinitusState.Win(
            date = date,
            guesses = guesses + inputWord,
        )
    }

    if (guesses.size >= Constants.MAX_ATTEMPTS - 1) {
        return MinitusState.Lose(
            date = date,
            guesses = guesses + inputWord,
        )
    }

    return copy(
        guesses = guesses + inputWord,
        lastInputError = null,
    )
}

private fun playingScreen(
    state: MinitusState.Playing,
    expectedWord: String,
) = buildVideotex {
    clearAll()
    displayLogo()

    displayGameGrid(
        guesses = state.guesses,
        expectedWord = expectedWord,
    )

    if (state.lastInputError != null) {
        withTextColor(Color.Red) {
            when (state.lastInputError) {
                MinitusState.Error.InvalidLength -> {
                    appendLine(
                        "Le mot à trouver contient ${expectedWord.length} lettres."
                    )
                }

                MinitusState.Error.NotInDictionary -> {
                    appendLine("Désolé, ce mot n'est pas dans mon")
                    appendLine("dictionnaire.")
                }
            }
        }
    }

    moveCursorTo(1, 24)
    append("Entrez un mot + ")
    withInvertedBackground {
        appendLine("ENVOI")
    }
}

private fun winScreen(
    state: MinitusState.Win,
    expectedWord: String,
) = buildVideotex {
    clearAll()
    displayLogo()

    displayGameGrid(
        guesses = state.guesses,
        expectedWord = expectedWord,
    )

    withTextColor(Color.Green) {
        appendLine("Bravo, vous avez trouvé le mot du jour !")
    }
}

private fun loseScreen(
    state: MinitusState.Lose,
    expectedWord: String,
) = buildVideotex {
    clearAll()
    displayLogo()

    displayGameGrid(
        guesses = state.guesses,
        expectedWord = expectedWord,
    )

    withTextColor(Color.Red) {
        appendLine("Désolé, vous n'avez pas trouvé le mot du jour. :(")
        appendLine()

        append("Le mot était ")
        withInvertedBackground {
            appendLine(expectedWord)
        }
    }
}

private fun VideotexBuilder.displayGameGrid(
    guesses: List<String>,
    expectedWord: String,
) {
    withCharacterSize(CharacterSize.DoubleSize) {
        guesses.forEach { guess ->
            appendLine()
            append(guess)
        }

        if (guesses.size < Constants.MAX_ATTEMPTS) {
            appendLine()
            withInvertedBackground {
                withTextColor(Color.Red) {
                    append(expectedWord.first())
                }
            }
            appendLine(
                expectedWord
                    .drop(1)
                    .map { '.' }
                    .joinToString(separator = "")
            )
        }

        (guesses.size + 1 until Constants.MAX_ATTEMPTS).forEach { _ ->
            // On affiche des lignes vides pour remplir les guess non-faits
            appendLine()
            appendLine(
                expectedWord
                    .map { '.' }
                    .joinToString(separator = "")
            )
        }
    }

    appendLine()
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

    withUnderline {
        repeatChar(' ', 39)
    }

    appendLine()
    appendLine()
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
