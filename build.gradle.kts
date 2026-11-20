plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.kotlin.binaryCompatibilityValidator) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.spotless) apply false

    alias(libs.plugins.dokka)
}

val libraryGroup = "fr.outadoc.minipavi"
val libraryVersion: String = findProperty("fr.outadoc.minipavi.version") as String

subprojects {
    group = libraryGroup
    version = libraryVersion

    plugins.withId("maven-publish") {
        extensions.configure<PublishingExtension> {
            repositories {
                // https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry
                maven {
                    name = "GitHubPackages"
                    url = uri("https://maven.pkg.github.com/outadoc/minipavi-kotlin")
                    credentials {
                        username = System.getenv("GITHUB_ACTOR")
                        password = System.getenv("GITHUB_TOKEN")
                    }
                }
            }
        }
    }
}

tasks.dokkaHtmlMultiModule {
    moduleVersion.set(libraryVersion)
}
