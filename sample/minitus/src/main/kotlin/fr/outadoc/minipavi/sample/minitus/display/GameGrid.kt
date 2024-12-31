package fr.outadoc.minipavi.sample.minitus.display

import fr.outadoc.minipavi.sample.minitus.CharacterMatch
import fr.outadoc.minipavi.sample.minitus.GameConstants
import fr.outadoc.minipavi.sample.minitus.computeDiff
import fr.outadoc.minipavi.sample.minitus.getHintForNextGuess
import fr.outadoc.minipavi.videotex.CharacterSize
import fr.outadoc.minipavi.videotex.Color
import fr.outadoc.minipavi.videotex.VideotexBuilder

internal fun VideotexBuilder.displayGameGrid(
    guesses: List<String>,
    expectedWord: String,
    showExtraLines: Boolean,
) {
    val startPadding = (20 - expectedWord.length) / 2

    withCharacterSize(CharacterSize.DoubleSize) {
        guesses.forEach { guess ->
            appendLine()
            repeatChar(' ', startPadding)

            val diff =
                computeDiff(
                    expectedWord = expectedWord,
                    guess = guess,
                )

            displayDiffLine(diff)
            appendLine()
        }

        if (showExtraLines) {
            if (guesses.size < GameConstants.MAX_ATTEMPTS) {
                appendLine()
                repeatChar(' ', startPadding)

                displayDiffLine(
                    getHintForNextGuess(
                        expectedWord = expectedWord,
                        previousGuesses = guesses,
                    ),
                )
                appendLine()
            }

            (guesses.size + 1 until GameConstants.MAX_ATTEMPTS).forEach { _ ->
                // On affiche des lignes vides pour remplir les guess non-faits
                appendLine()
                repeatChar(' ', startPadding)

                appendLine(
                    expectedWord
                        .map { '.' }
                        .joinToString(separator = ""),
                )
            }
        }
    }

    appendLine()
    appendLine()
}

private fun VideotexBuilder.displayDiffLine(guess: List<CharacterMatch>) {
    guess.forEach { match ->
        when (match) {
            is CharacterMatch.Exact -> {
                withInvertedBackground {
                    withTextColor(Color.Red) {
                        append(match.character)
                    }
                }
            }

            is CharacterMatch.Partial -> {
                withInvertedBackground {
                    withTextColor(Color.Yellow) {
                        append(match.character)
                    }
                }
            }

            is CharacterMatch.None -> {
                append(match.character)
            }
        }
    }
}
