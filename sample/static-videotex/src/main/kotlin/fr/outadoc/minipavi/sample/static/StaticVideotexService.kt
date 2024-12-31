package fr.outadoc.minipavi.sample.static

import fr.outadoc.minipavi.core.ktor.minitelService
import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minipavi.videotex.buildVideotex
import io.ktor.server.application.Application
import kotlinx.serialization.Serializable

@Serializable
data class StaticSampleVdtState(
    val iter: Int = 0,
)

private val sampleFiles =
    listOf(
        "0.vdt",
        "1.vdt",
        "2.vdt",
        "3.vdt",
        "4.vdt",
        "5.vdt",
        "6.vdt",
        "france.vdt",
        "gigazertek.vdt",
        "masquage.vdt",
        "minitel-rulez.vdt",
        "minitel-wants-you.vdt",
        "mitterrand.vdt",
        "tux.vdt",
    )

/**
 * Un service qui affiche tous les exemples de fichiers vidéotex en boucle.
 */
fun Application.staticSampleVdt() {
    minitelService<StaticSampleVdtState>(
        path = "/",
        version = "0.1",
        initialState = StaticSampleVdtState(),
    ) { request ->
        val state = request.state
        val file = sampleFiles[state.iter % sampleFiles.size]
        ServiceResponse(
            content =
                buildVideotex {
                    clearStatus()
                    resetCharacterSets()

                    appendRawVideotex(
                        readResource("/static/$file"),
                    )

                    moveCursorTo(1, 0)
                    resetCharacterSets()
                    append("> $file")
                },
            state =
                state.copy(
                    iter = state.iter + 1,
                ),
        )
    }
}
