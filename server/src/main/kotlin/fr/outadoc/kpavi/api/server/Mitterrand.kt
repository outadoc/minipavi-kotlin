package fr.outadoc.kpavi.api.server

import fr.outadoc.kpavi.api.data.minitelApp
import fr.outadoc.kpavi.api.domain.model.ServiceResponse
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data object MitterrandState

fun Route.mitterrand() {
    minitelApp<MitterrandState>(version = "0.1") {
        ServiceResponse(
            content = readResource("/static/mitterrand.vdt"),
            context = MitterrandState
        )
    }
}
