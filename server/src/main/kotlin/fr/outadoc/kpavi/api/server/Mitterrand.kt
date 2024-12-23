package fr.outadoc.kpavi.api.server

import fr.outadoc.kpavi.api.data.model.ServiceResponse
import io.ktor.server.routing.*

fun Route.mitterrand() {
    miniRoute("/") {
        ServiceResponse(
            version = "0.1",
            content = readResource("/static/mitterrand.vdt"),
            context = ""
        )
    }
}
