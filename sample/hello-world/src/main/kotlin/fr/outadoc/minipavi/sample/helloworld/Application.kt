package fr.outadoc.minipavi.sample.helloworld

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::helloWorld,
    ).start(wait = true)
}
