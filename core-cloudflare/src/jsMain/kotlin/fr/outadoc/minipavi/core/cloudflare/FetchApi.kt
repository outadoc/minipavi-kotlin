package fr.outadoc.minipavi.core.cloudflare

import kotlin.js.Promise

/**
 * Requête HTTP reçue par un Worker, telle que définie par l'API Fetch.
 *
 * Seuls les membres utilisés par ce module sont déclarés.
 */
public external class Request(
    input: String,
    init: RequestInit = definedExternally,
) {
    /** La méthode HTTP de la requête. */
    public val method: String

    /** L'URL complète de la requête. */
    public val url: String

    /** Lit le corps de la requête comme texte. */
    public fun text(): Promise<String>
}

/**
 * Options de construction d'une [Request].
 */
public external interface RequestInit {
    /** La méthode HTTP de la requête. */
    public var method: String?

    /** Le corps de la requête. */
    public var body: String?
}

/**
 * Réponse HTTP renvoyée par un Worker, telle que définie par l'API Fetch.
 *
 * Seuls les membres utilisés par ce module sont déclarés.
 */
public external class Response(
    body: String?,
    init: ResponseInit = definedExternally,
) {
    /** Le code de statut HTTP de la réponse. */
    public val status: Int

    /** Les en-têtes de la réponse. */
    public val headers: Headers

    /** Lit le corps de la réponse comme texte. */
    public fun text(): Promise<String>
}

/**
 * Options de construction d'une [Response].
 */
public external interface ResponseInit {
    /** Le code de statut HTTP de la réponse. */
    public var status: Int?

    /** Les en-têtes de la réponse. */
    public var headers: Headers?
}

/**
 * En-têtes HTTP, tels que définis par l'API Fetch.
 */
public external class Headers {
    /** Renvoie la valeur de l'en-tête [name], ou `null` s'il est absent. */
    public fun get(name: String): String?

    /** Définit la valeur de l'en-tête [name]. */
    public fun set(
        name: String,
        value: String,
    )
}

internal external class URL(
    url: String,
) {
    val pathname: String
}
