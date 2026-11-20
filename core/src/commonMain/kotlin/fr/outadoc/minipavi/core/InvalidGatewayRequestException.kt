package fr.outadoc.minipavi.core

/**
 * Levée lorsque le corps d'une requête ne correspond pas au format attendu
 * d'une requête de la passerelle MiniPavi.
 *
 * Les modules d'intégration la traduisent en réponse HTTP 400.
 */
public class InvalidGatewayRequestException(
    cause: Throwable,
) : Exception("Invalid MiniPavi gateway request: ${cause.message}", cause)
