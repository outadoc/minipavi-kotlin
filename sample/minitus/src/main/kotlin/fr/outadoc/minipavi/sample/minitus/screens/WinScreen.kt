package fr.outadoc.minipavi.sample.minitus.screens

import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minipavi.sample.minitus.display.displayGameGrid
import fr.outadoc.minipavi.sample.minitus.display.displayLogo
import fr.outadoc.minipavi.videotex.Color
import fr.outadoc.minipavi.videotex.buildVideotex

internal fun winScreen(
    state: MinitusState.Win,
    expectedWord: String,
): ServiceResponse<MinitusState> =
    ServiceResponse(
        state = state,
        content =
            buildVideotex {
                clearAll()
                displayLogo()

                displayGameGrid(
                    guesses = state.guesses,
                    expectedWord = expectedWord,
                    showExtraLines = false,
                )

                withTextColor(Color.Green) {
                    appendLine("C'est gagné !")
                }
            },
    )
