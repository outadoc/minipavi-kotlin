import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URL

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.binaryCompatibilityValidator)
    alias(libs.plugins.spotless)
}

group = "fr.outadoc.pavikt"
version = "1.0.0"

kotlin {
    explicitApi()

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.io)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.contentNegotiation)
                implementation(libs.ktor.serialization.json)
                implementation(libs.kotlinx.serialization.json)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.kotlin.test.junit)
            }
        }
    }
}

spotless {
    kotlin {
        ktlint()
    }
}

tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets {
        configureEach {
            includes.from("README.md")

            externalDocumentationLink {
                url.set(URL("https://kotlinlang.org/api/kotlinx.serialization/"))
            }

            externalDocumentationLink {
                url.set(URL("https://kotlinlang.org/api/kotlinx-io/"))
            }
        }
    }
}
