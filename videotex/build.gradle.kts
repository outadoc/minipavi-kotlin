import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
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
                implementation(libs.kotlinx.io)
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
        }
    }
}
