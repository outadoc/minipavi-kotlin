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
                api(projects.core)
            }
        }

        val jvmMain by getting {
            dependencies {
                api(libs.ktor.server.core)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlin.test.junit)
                implementation(libs.ktor.server.testHost)
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

            externalDocumentationLink {
                url.set(URL("https://api.ktor.io/ktor-server"))
                packageListUrl.set(URL("https://api.ktor.io/package-list"))
            }
        }
    }
}

publishing {
    publications {
        withType<MavenPublication>().configureEach {
            pom {
                name.set("minipavi-core-ktor")
                description.set("Intégration Ktor du SDK MiniPavi-Kotlin.")
                url.set("https://github.com/outadoc/minipavi-kotlin")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
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
