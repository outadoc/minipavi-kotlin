plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.kotlin.binaryCompatibilityValidator) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.spotless) apply false

    alias(libs.plugins.dokka)
}

tasks.dokkaHtmlMultiModule {
    moduleVersion.set(findProperty("fr.outadoc.minipavi.version") as String)
}
