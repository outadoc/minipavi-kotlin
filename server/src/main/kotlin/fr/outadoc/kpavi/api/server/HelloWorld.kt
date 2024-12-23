package fr.outadoc.kpavi.api.server

import fr.outadoc.kpavi.api.data.model.ServiceResponse
import fr.outadoc.kpavi.buildVideotex
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.helloWorld() {
    post("/") {
        val request = call.getPaviRequest<String>()
        call.respond(
            ServiceResponse(
                version = "0.1",
                content = buildVideotex {
                    clearScreen()
                    append("Hello, world!")
                },
                context = ""
            )
        )
    }
}
