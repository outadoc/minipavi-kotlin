import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.dokka)
}

group = "fr.outadoc.dokkaissue"
version = "0.0.1"

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
            }
        }
    }
}

tasks.dokkaHtmlPartial {
    dokkaSourceSets {
        configureEach {
            reportUndocumented.set(true)
            failOnWarning.set(true)
        }
    }
}
