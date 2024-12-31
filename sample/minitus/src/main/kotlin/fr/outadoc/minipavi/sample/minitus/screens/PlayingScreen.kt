package fr.outadoc.minipavi.sample.minitus.screens

import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minipavi.sample.minitus.GameConstants
import fr.outadoc.minipavi.sample.minitus.dictionary.pickDailyWord
import fr.outadoc.minipavi.sample.minitus.display.displayGameGrid
import fr.outadoc.minipavi.sample.minitus.display.displayLogo
import fr.outadoc.minipavi.sample.minitus.normalize
import fr.outadoc.minipavi.videotex.Color
import fr.outadoc.minipavi.videotex.buildVideotex
import kotlinx.datetime.LocalDate

internal fun playingScreen(
    state: MinitusState.Playing,
    expectedWord: String,
): ServiceResponse<MinitusState> =
    ServiceResponse(
        state = state,
        command =
            ServiceResponse.Command.InputText(
                col = 24,
                line = 24,
                length = expectedWord.length,
            ),
        content =
            buildVideotex {
                clearAll()
                displayLogo()

                displayGameGrid(
                    guesses = state.guesses,
                    expectedWord = expectedWord,
                    showExtraLines = true,
                )

                if (state.lastInputError != null) {
                    withTextColor(Color.Red) {
                        when (state.lastInputError) {
                            MinitusState.Error.InvalidLength -> {
                                appendLine(
                                    "Le mot à trouver contient ${expectedWord.length} lettres.",
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
            },
    )

internal fun MinitusState.Playing.reduce(
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

    if (inputWord.length !in GameConstants.ALLOWED_WORD_LENGTHS) {
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

    if (guesses.size >= GameConstants.MAX_ATTEMPTS - 1) {
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
