package fr.outadoc.kpavi.api.server

import fr.outadoc.kpavi.api.data.model.ServiceResponse
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.io.readByteString
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json)
    }

    routing {
        post("/") {
            call.respond(
                ServiceResponse(
                    version = "0.1",
                    content = this::class.java.getResourceAsStream("/static/mitterrand.vdt")!!
                        .asSource()
                        .buffered()
                        .readByteString(),
                    context = "",
                )
            )
        }
    }
}
