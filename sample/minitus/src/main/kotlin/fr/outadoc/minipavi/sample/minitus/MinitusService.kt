package fr.outadoc.minipavi.sample.minitus

import fr.outadoc.minipavi.core.ktor.minitelService
import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minipavi.videotex.CharacterSize
import fr.outadoc.minipavi.videotex.buildVideotex
import io.ktor.server.application.Application
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface MinitusState {
    @Serializable
    @SerialName("intro")
    data object IntroPage : MinitusState
}

fun Application.minitus() {
    minitelService<MinitusState>(
        path = "/",
        version = "0.1",
        initialState = MinitusState.IntroPage,
    ) { request ->
        when (request.state) {
            MinitusState.IntroPage -> {
                ServiceResponse(
                    state = MinitusState.IntroPage,
                    content =
                        buildVideotex {
                            clearAll()
                            appendLine()

                            withCharacterSize(CharacterSize.DoubleHeight) {
                                appendLine("MINITUS")
                            }
                        },
                )
            }
        }
    }
}
