package fr.outadoc.pavikt.sample.services

import fr.outadoc.pavikt.minipavi.ktor.minitelService
import fr.outadoc.pavikt.minipavi.model.ServiceResponse
import fr.outadoc.pavikt.sample.utils.readResource
import io.ktor.server.application.*
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

fun Application.staticSampleVdt() {
    minitelService<StaticSampleVdtState>(
        path = "/",
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
