package fr.outadoc.kpavi.api.domain.model

data class GatewayRequest<T : Any>(
    val payload: Payload<T>,
    /**
     * Tableau associatif contenant les éventuels paramètres indiqués dans l'Url.
     */
    val urlParams: List<String>? = null,
) {
    data class Payload<T : Any>(
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
        val uniqueId: String,

        /**
         * Adresse IP (ou téléphone si connu) de
         * l'utilisateur connecté
         */
        val remoteAddress: String,

        /**
         * Type de la connexion.
         */
        val socketType: SocketType,

        /**
         * 3 caractères correspondant au type de Minitel, si connu. Sinon « ??? ».
         *
         * N'est pas forcément initialisé dès la connexion.
         */
        val minitelVersion: String,

        /**
         * Tableau contenant la saisie de l'utilisateur.
         *
         * Si il s'agit d'une saisie de plusieurs lignes, chaque ligne est un élément du tableau.
         * S'il n'y a qu'une seule ligne, la saisie est à l'indice « 0 » du tableau.
         */
        val userInput: List<String>,

        /**
         * Données libres précédemment envoyées par le service.
         *
         * Sert à sauvegarder le contexte de l'utilisateur tout au long de sa visite du service.
         */
        val state: T,

        /**
         * Touche de fonction saisie, ou évènement, ayant initié cette requête.
         */
        val function: Function
    )

    enum class SocketType {
        WebSocket,
        WebSocketSSL,
        Asterisk,
        Telnet,
    }

    enum class Function {
        ENVOI,
        SUITE,
        RETOUR,
        ANNULATION,
        CORRECTION,
        GUIDE,
        REPETITION,
        SOMMAIRE,
        CONNECTION,
        FIN,
        DIRECT,
        DIRECT_CONNECTION,
        DIRECT_CALL_FAILED,
        DIRECT_CALL_ENDED,
        BACKGROUND_CALL,
        SIMULATED_BACKGROUND_CALL,
    }
}
