package fr.outadoc.pavikt.api.server

import fr.outadoc.pavikt.api.data.minitelApp
import fr.outadoc.pavikt.api.domain.model.ServiceResponse
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class StaticSampleVdtState(
    val iter: Int = 0
)

private val sampleFiles = listOf(
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
    "teletext.vdt",
    "tux.vdt",
)

fun Route.staticSampleVdt() {
    minitelApp<StaticSampleVdtState>(
        version = "0.1",
        initialState = StaticSampleVdtState()
    ) { request ->
        val state = request.payload.state
        val file = sampleFiles[state.iter % sampleFiles.size]
        ServiceResponse(
            content = readResource("/static/${file}"),
            state = state.copy(
                iter = state.iter + 1
            )
        )
    }
}
