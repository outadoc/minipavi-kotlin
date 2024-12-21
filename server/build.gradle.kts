plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinx.serialization)
    application
}

group = "fr.outadoc.kpavi"
version = "1.0.0"
application {
    mainClass.set("fr.outadoc.kpavi.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {

    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
}