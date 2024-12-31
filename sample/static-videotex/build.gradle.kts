plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.spotless)
    application
}

application {
    mainClass.set("fr.outadoc.minipavi.sample.static.ApplicationKt")
    applicationDefaultJvmArgs = listOf(
        "-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}"
    )
}

dependencies {
    implementation(projects.core)
    implementation(projects.videotex)

    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.kotlinx.serialization.json)
}

spotless {
    kotlin {
        ktlint()
    }
}
