import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URL

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.binaryCompatibilityValidator)
    alias(libs.plugins.spotless)
    `maven-publish`
}

group = "fr.outadoc.minipavi"
version = findProperty("fr.outadoc.minipavi.version") as String

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

tasks.dokkaHtmlPartial {
    dokkaSourceSets {
        configureEach {
            includes.from("README.md")
            reportUndocumented.set(true)
            failOnWarning.set(true)

            externalDocumentationLink {
                url.set(URL("https://kotlinlang.org/api/kotlinx.serialization/"))
            }

            externalDocumentationLink {
                url.set(URL("https://kotlinlang.org/api/kotlinx-io/"))
            }
        }
    }
}

publishing {
    publications {
        getByName<MavenPublication>("kotlinMultiplatform") {
            pom {
                name.set("minipavi-core")
                description.set("SDK pour le développement de services Minitel sur la plateforme MiniPavi en Kotlin.")
                url.set("https://github.com/outadoc/minipavi-kotlin")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("outadoc")
                        name.set("Baptiste Candellier")
                        email.set("baptiste@candellier.me")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/outadoc/minipavi-kotlin.git")
                    developerConnection.set("scm:git:ssh://github.com/outadoc/minipavi-kotlin.git")
                    url.set("https://github.com/outadoc/minipavi-kotlin")
                }
            }
        }
    }

    repositories {
        mavenLocal()
    }
}
