package fr.outadoc.kpavi.api.server

import fr.outadoc.kpavi.api.data.model.ServiceResponse
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.mitterrand() {
    post("/") {
        call.respond(
            ServiceResponse(
                version = "0.1",
                content = readResource("/static/mitterrand.vdt"),
                context = "",
            )
        )
    }
}
