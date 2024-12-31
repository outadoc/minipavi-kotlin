package fr.outadoc.minipavi.sample.minitus

import io.ktor.server.application.Application

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    minitus()
}
