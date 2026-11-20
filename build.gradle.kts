plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.kotlin.binaryCompatibilityValidator) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.spotless) apply false

    alias(libs.plugins.dokka)
}

// JitPack publishes under com.github.<user>.<repo> with the tag or commit as version. The POMs
// of modules that depend on each other must use those coordinates too, or they won't resolve.
val isJitPack: Boolean = System.getenv("JITPACK") == "true"
val libraryGroup: String = if (isJitPack) "com.github.outadoc.minipavi-kotlin" else "fr.outadoc.minipavi"
val libraryVersion: String =
    System.getenv("VERSION")?.takeIf { isJitPack && it.isNotBlank() }
        ?: findProperty("fr.outadoc.minipavi.version") as String

subprojects {
    group = libraryGroup
    version = libraryVersion
}

tasks.dokkaHtmlMultiModule {
    moduleVersion.set(libraryVersion)
}
