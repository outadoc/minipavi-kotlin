package fr.outadoc.minipavi.core.internal

import kotlinx.serialization.json.Json

internal val KJson: Json = Json {
    ignoreUnknownKeys = true
}
