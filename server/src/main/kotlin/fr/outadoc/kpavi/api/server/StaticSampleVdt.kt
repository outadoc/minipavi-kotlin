package fr.outadoc.kpavi.api.server

import fr.outadoc.kpavi.api.data.minitelApp
import fr.outadoc.kpavi.api.domain.model.ServiceResponse
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
    minitelApp<StaticSampleVdtState>(version = "0.1") { request ->
        val state = request.payload.context ?: StaticSampleVdtState()
        val file = sampleFiles[state.iter % sampleFiles.size]
        ServiceResponse(
            content = readResource("/static/${file}"),
            context = state.copy(
                iter = state.iter + 1
            )
        )
    }
}
