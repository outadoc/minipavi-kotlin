plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.spotless)
}

kotlin {
    js {
        moduleName = "cloudflare-worker"
        nodejs()
        useEsModules()
        binaries.library()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(projects.coreCloudflare)
                implementation(projects.videotex)
            }
        }
    }
}

spotless {
    kotlin {
        ktlint()
    }
}
