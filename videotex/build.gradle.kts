import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URL

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.binaryCompatibilityValidator)
    alias(libs.plugins.spotless)
    `maven-publish`
}

group = "fr.outadoc.minipavi"
version = "0.0.3"

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
                name.set("minipavi-videotex")
                description.set("Module de création de documents Vidéotex en Kotlin.")
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
