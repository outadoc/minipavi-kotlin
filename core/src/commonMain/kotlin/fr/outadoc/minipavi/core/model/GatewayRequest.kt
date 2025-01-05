package fr.outadoc.minipavi.core.model

/**
 * Données émises par la passerelle, à destination du service.
 */
public data class GatewayRequest<TState : Any>(
    /**
     * Version de la passerelle.
     */
    val gatewayVersion: String,

    /**
     * Trois caractères correspondant au type de Minitel, si connu. Sinon `???`.
     *
     * Ce champ n'est pas forcément initialisé dès la connexion.
     */
    val minitelVersion: String,

    /**
     * Identifiant unique de l'utilisateur connecté au service.
     *
     * Les 4 derniers chiffres correspondent au code PIN pour l'utilisation
     * de l'accès WebMedia.
     */
    val userId: String,

    /**
     * Adresse IP (ou téléphone si connu) de l'utilisateur connecté.
     */
    val remoteAddress: String,

    /**
     * Type de la connexion.
     */
    val socketType: SocketType,

    /**
     * Données libres précédemment envoyées par le service.
     *
     * Sert à sauvegarder le contexte de l'utilisateur tout au long de sa visite du service.
     */
    val state: TState,

    /**
     * Évènement ayant initié cette requête.
     */
    val event: Event,

    /**
     * Tableau contenant la saisie de l'utilisateur.
     *
     * S'il s'agit d'une saisie de plusieurs lignes, chaque ligne est un élément du tableau.
     * S'il n'y a qu'une seule ligne, la saisie est à l'indice `0` du tableau.
     */
    val userInput: List<String>,

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
     * Évènement ayant initié une requête.
     */
    public sealed class Event {

        /**
         * Événement envoyé lors de l'appui sur une touche du clavier.
         */
        public data class KeyboardInput(
            /**
             * Touche du clavier appuyée.
             */
            val key: FunctionKey,
        ) : Event()

        /**
         * Événement envoyé lors de la connexion initiale de l'utilisateur.
         */
        public data object Connection : Event()

        /**
         * Événement envoyé lors de la déconnexion de l'utilisateur.
         */
        public data object Disconnection : Event()

        /**
         * Événement envoyé lorsque la passerelle a appelé le service
         * à cause d'un paramètre [ServiceResponse.directCall] à
         * [ServiceResponse.DirectCallSetting.Yes].
         */
        public data object Direct : Event()

        /**
         * Événement envoyé lorsque la passerelle a appelé le service
         * à cause d'un paramètre [ServiceResponse.directCall] à
         * [ServiceResponse.DirectCallSetting.YesCnx].
         */
        public data object DirectConnection : Event()

        /**
         * Événement envoyé lors de l'échec de l'appel direct,
         * suite à l'utilisation du paramètre [ServiceResponse.directCall].
         */
        public data object DirectCallFailed : Event()

        /**
         * Événement envoyé lorsque l'appel direct a été terminé,
         * suite à l'utilisation du paramètre [ServiceResponse.directCall].
         */
        public data object DirectCallEnded : Event()

        /**
         * Événement envoyé lorsque la passerelle a appelé le service
         * suite à une commande [ServiceResponse.Command.BackgroundCall].
         */
        public data object BackgroundCall : Event()

        /**
         * Événement envoyé lorsque la passerelle a appelé le service
         * suite à une commande [ServiceResponse.Command.BackgroundCall]
         * avec le paramètre [ServiceResponse.Command.BackgroundCall.simulate] à `true`.
         */
        public data object BackgroundCallSimulated : Event()
    }
}
