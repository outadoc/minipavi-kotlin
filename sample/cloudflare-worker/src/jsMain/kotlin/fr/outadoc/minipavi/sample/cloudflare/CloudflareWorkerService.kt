@file:OptIn(ExperimentalJsExport::class)

package fr.outadoc.minipavi.sample.cloudflare

import fr.outadoc.minipavi.core.MinitelServiceHandler
import fr.outadoc.minipavi.core.cloudflare.ConsoleMinitelLogger
import fr.outadoc.minipavi.core.cloudflare.Request
import fr.outadoc.minipavi.core.cloudflare.Response
import fr.outadoc.minipavi.core.cloudflare.toFetchHandler
import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minipavi.videotex.CharacterSize
import fr.outadoc.minipavi.videotex.Color
import fr.outadoc.minipavi.videotex.buildVideotex
import kotlinx.serialization.Serializable
import kotlin.js.Promise

@Serializable
data class VisitCounter(
    val visits: Int,
)

/**
 * Un service qui compte les pages affichées pendant la session.
 */
private val handler =
    MinitelServiceHandler<VisitCounter>(
        version = "0.1",
        initialState = { VisitCounter(visits = 0) },
        logger = ConsoleMinitelLogger(),
    ) { request ->
        val visits = request.state.visits + 1
        ServiceResponse(
            state = VisitCounter(visits = visits),
            content =
                buildVideotex {
                    clearAll()
                    appendLine()

                    withCharacterSize(CharacterSize.DoubleHeight) {
                        appendLine(" Bonjour depuis Cloudflare !")
                    }
                    appendLine()

                    append(" Pages vues : ")
                    withTextColor(Color.Yellow) {
                        appendLine(visits.toString())
                    }

                    appendLine()
                    appendLine(" Appuyez sur ENVOI pour continuer.")
                },
        )
    }.toFetchHandler(path = "/")

/**
 * Point d'entrée du Worker, réexporté par `worker/index.mjs`.
 *
 * Une fonction, et non une propriété : Kotlin/JS exporte les propriétés sous forme d'accesseurs.
 */
@JsExport
fun fetch(request: Request): Promise<Response> = handler(request)
