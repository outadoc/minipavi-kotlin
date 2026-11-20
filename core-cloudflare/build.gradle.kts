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

    js {
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.core)
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

apiValidation {
    @OptIn(kotlinx.validation.ExperimentalBCVApi::class)
    klib {
        enabled = true
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
        withType<MavenPublication>().configureEach {
            pom {
                name.set("minipavi-core-cloudflare")
                description.set("Intégration Cloudflare Workers du SDK MiniPavi-Kotlin.")
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
