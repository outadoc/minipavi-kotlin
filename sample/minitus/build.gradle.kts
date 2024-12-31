import com.google.cloud.tools.gradle.appengine.appyaml.AppEngineAppYamlExtension

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.spotless)
    id("com.google.cloud.tools.appengine") version "2.8.0"
    id("com.gradleup.shadow")
    application
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

configure<AppEngineAppYamlExtension> {
    stage {
        setArtifact("build/libs/${project.name}-all.jar")
    }
    deploy {
        version = "GCLOUD_CONFIG"
        projectId = "GCLOUD_CONFIG"
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.videotex)

    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.kotlin.test.junit)
}

spotless {
    kotlin {
        ktlint()
    }
}
