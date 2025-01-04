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
    /**
     * Le type de connexion de l'utilisateur.
     */
    public enum class SocketType {
        /**
         * Connexion par WebSocket.
         */
        WebSocket,

        /**
         * Connexion par WebSocket sécurisée.
         */
        WebSocketSSL,

        /**
         * Connexion par [Asterisk](https://fr.wikipedia.org/wiki/Asterisk_(logiciel))
         * (réseau téléphonique).
         */
        Asterisk,

        /**
         * Connexion par Telnet.
         */
        Telnet,
    }

    /**
     * Touche de fonction ou évènement ayant initié une requête.
     */
    public enum class Function {
        /**
         * Touche `ENVOI`.
         */
        Envoi,

        /**
         * Touche `SUITE`.
         */
        Suite,

        /**
         * Touche `Retour`.
         */
        Retour,

        /**
         * Touche `Annulation`.
         */
        Annulation,

        /**
         * Touche `Correction`.
         */
        Correction,

        /**
         * Touche `GUIDE`.
         */
        Guide,

        /**
         * Touche `RÉPÉTITION`.
         */
        Repetition,

        /**
         * Touche `SOMMAIRE`.
         */
        Sommaire,

        /**
         * Événement envoyé lors de la connexion initiale de l'utilisateur.
         */
        Connection,

        /**
         * Événement envoyé lors de la déconnexion de l'utilisateur.
         */
        Fin,

        /**
         * Événement envoyé lorsque la passerelle a appelé le service
         * à cause d'un paramètre [ServiceResponse.directCall] à
         * [ServiceResponse.DirectCallSetting.Yes].
         */
        Direct,

        /**
         * Événement envoyé lorsque la passerelle a appelé le service
         * à cause d'un paramètre [ServiceResponse.directCall] à
         * [ServiceResponse.DirectCallSetting.YesCnx].
         */
        DirectConnection,

        /**
         * Événement envoyé lors de l'échec de l'appel direct,
         * suite à l'utilisation du paramètre [ServiceResponse.directCall].
         */
        DirectCallFailed,

        /**
         * Événement envoyé lorsque l'appel direct a été terminé,
         * suite à l'utilisation du paramètre [ServiceResponse.directCall].
         */
        DirectCallEnded,

        /**
         * Événement envoyé lorsque la passerelle a appelé le service
         * suite à une commande [ServiceResponse.Command.BackgroundCall].
         */
        BackgroundCall,

        /**
         * Événement envoyé lorsque la passerelle a appelé le service
         * suite à une commande [ServiceResponse.Command.BackgroundCall]
         * avec le paramètre [ServiceResponse.Command.BackgroundCall.simulate] à `true`.
         */
        BackgroundCallSimulated,
    }
}
