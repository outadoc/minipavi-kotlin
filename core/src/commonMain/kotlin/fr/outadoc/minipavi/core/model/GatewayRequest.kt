package fr.outadoc.minipavi.core.model

/**
 * Données émises par la passerelle, à destination du service.
 */
public data class GatewayRequest<T : Any>(
    /**
     * Version de la passerelle.
     */
    val gatewayVersion: String,

    /**
     * Identifiant unique de l'utilisateur connecté au service.
     *
     * Les 4 derniers chiffres correspondent au code pin pour l'utilisation
     * de l'accès WebMedia.
     */
    val sessionId: String,

    /**
     * Adresse IP (ou téléphone si connu) de l'utilisateur connecté.
     */
    val remoteAddress: String,

    /**
     * Type de la connexion.
     */
    val socketType: SocketType,

    /**
     * Trois caractères correspondant au type de Minitel, si connu. Sinon `???`.
     *
     * Ce champ n'est pas forcément initialisé dès la connexion.
     */
    val minitelVersion: String,

    /**
     * Tableau contenant la saisie de l'utilisateur.
     *
     * S'il s'agit d'une saisie de plusieurs lignes, chaque ligne est un élément du tableau.
     * S'il n'y a qu'une seule ligne, la saisie est à l'indice `0` du tableau.
     */
    val userInput: List<String>,

    /**
     * Données libres précédemment envoyées par le service.
     *
     * Sert à sauvegarder le contexte de l'utilisateur tout au long de sa visite du service.
     */
    val state: T,

    /**
     * Touche de fonction ou évènement ayant initié cette requête.
     */
    val function: Function,

    /**
     * Tableau associatif contenant les éventuels paramètres indiqués dans l'URL.
     */
    val urlParams: Map<String, String>? = null,
) {
    public enum class SocketType {
        WebSocket,
        WebSocketSSL,
        Asterisk,
        Telnet,
    }

    public enum class Function {
        Envoi,
        Suite,
        Retour,
        Annulation,
        Correction,
        Guide,
        Repetition,
        Sommaire,
        Connection,
        Fin,
        Direct,
        DirectConnection,
        DirectCallFailed,
        DirectCallEnded,
        BackgroundCall,
        BackgroundCallSimulated,
    }
}
