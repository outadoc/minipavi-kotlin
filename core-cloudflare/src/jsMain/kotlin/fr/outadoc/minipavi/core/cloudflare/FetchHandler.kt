package fr.outadoc.minipavi.core.cloudflare

import fr.outadoc.minipavi.core.InvalidGatewayRequestException
import fr.outadoc.minipavi.core.MinitelLogger
import fr.outadoc.minipavi.core.MinitelServiceHandler
import kotlin.js.Promise

/**
 * Transforme ce [MinitelServiceHandler] en gestionnaire `fetch` pour Cloudflare Workers.
 *
 * Le gestionnaire renvoie :
 * - 404 si le chemin de l'URL ne correspond pas à [path] ;
 * - 405 si la méthode n'est pas `POST` ;
 * - 400 si le corps n'est pas une requête valide de la passerelle ;
 * - 200 avec la réponse JSON du service sinon.
 *
 * Appelez le résultat depuis une fonction `fetch` exportée avec `@JsExport` (une fonction, et non une
 * propriété, que Kotlin/JS exporterait sous forme d'accesseur), puis réexportez-la depuis le point
 * d'entrée JS de votre Worker :
 *
 * ```kotlin
 * private val handler = MinitelServiceHandler(...).toFetchHandler()
 *
 * @JsExport
 * fun fetch(request: Request): Promise<Response> = handler(request)
 * ```
 *
 * ```js
 * import { fetch } from "./mon-service.mjs";
 * export default { fetch };
 * ```
 *
 * @param path Le chemin auquel le service répond. Par exemple, `/`, ou `/mon-service`.
 * Il doit correspondre exactement au chemin de l'URL : contrairement aux routes Ktor, il n'accepte
 * ni paramètre, ni joker, ni barre oblique finale supplémentaire.
 * @param logger Le [fr.outadoc.minipavi.core.MinitelLogger] qui recevra les requêtes invalides.
 */
public fun <TState : Any> MinitelServiceHandler<TState>.toFetchHandler(
    path: String = "/",
    logger: MinitelLogger = ConsoleMinitelLogger(),
): (Request) -> Promise<Response> = { request -> handleFetch(handler = this, path = path, logger = logger, request = request) }

private fun <TState : Any> handleFetch(
    handler: MinitelServiceHandler<TState>,
    path: String,
    logger: MinitelLogger,
    request: Request,
): Promise<Response> {
    if (URL(request.url).pathname != path) {
        return Promise.resolve(textResponse(status = 404, body = "Not Found"))
    }

    if (request.method != "POST") {
        return Promise.resolve(
            textResponse(status = 405, body = "Method Not Allowed", extraHeaders = mapOf("allow" to "POST")),
        )
    }

    return request.text().then { body ->
        try {
            jsonResponse(handler.handle(body))
        } catch (e: InvalidGatewayRequestException) {
            logger.log(MinitelLogger.Level.Warn, e) { "Received an invalid request" }
            textResponse(status = 400, body = "Bad Request")
        }
    }
}

private fun jsonResponse(body: String): Response =
    Response(
        body = body,
        init =
            responseInit(
                status = 200,
                contentType = "application/json; charset=utf-8",
            ),
    )

private fun textResponse(
    status: Int,
    body: String,
    extraHeaders: Map<String, String> = emptyMap(),
): Response =
    Response(
        body = body,
        init =
            responseInit(
                status = status,
                contentType = "text/plain; charset=utf-8",
                extraHeaders = extraHeaders,
            ),
    )

private fun responseInit(
    status: Int,
    contentType: String,
    extraHeaders: Map<String, String> = emptyMap(),
): ResponseInit {
    val headers = Headers()
    headers.set("content-type", contentType)
    extraHeaders.forEach { (name, value) -> headers.set(name, value) }

    val init = js("{}").unsafeCast<ResponseInit>()
    init.status = status
    init.headers = headers
    return init
}
