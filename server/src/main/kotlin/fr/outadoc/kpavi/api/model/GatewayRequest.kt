package fr.outadoc.kpavi.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GatewayRequest(
    @SerialName("PAVI")
    val payload: Payload,
    /**
     * Tableau associatif contenant les éventuels paramètres indiqués dans l'Url.
     */
    @SerialName("URLPARAMS")
    val urlParams: List<String>? = null,
) {
    @Serializable
    data class Payload(
        /**
         * Version de la passerelle.
         */
        @SerialName("version")
        val version: String,

        /**
         * Identifiant unique de l'utilisateur connecté au service.
         *
         * Les 4 derniers chiffres correspondent au code pin pour l'utilisation
         * de l'accès WebMedia.
         */
        @SerialName("uniqueId")
        val uniqueId: String,

        /**
         * Adresse IP (ou téléphone si connu) de
         * l'utilisateur connecté
         */
        @SerialName("remoteAddr")
        val remoteAddress: String,

        /**
         * Type de la connexion.
         */
        @SerialName("typesocket")
        val socketType: SocketType,

        /**
         * 3 caractères correspondant au type de Minitel, si connu. Sinon « ??? ».
         *
         * N'est pas forcément initialisé dès la connexion.
         */
        @SerialName("versionminitel")
        val minitelVersion: String,

        /**
         * Tableau contenant la saisie de l'utilisateur.
         *
         * Si il s'agit d'une saisie de plusieurs lignes, chaque ligne est un élément du tableau.
         * S'il n'y a qu'une seule ligne, la saisie est à l'indice « 0 » du tableau.
         */
        @SerialName("content")
        val content: List<String>,

        /**
         * Données libres précédemment envoyées par le service.
         *
         * Sert à sauvegarder le contexte de l'utilisateur tout au long de sa visite du service.
         */
        @SerialName("context")
        val context: String,

        /**
         * Touche de fonction saisie, ou évènement, ayant initié cette requête.
         */
        @SerialName("fctn")
        val function: Function
    )

    enum class SocketType {
        @SerialName("websocket")
        WebSocket,

        @SerialName("websocketssl")
        WebSocketSSL,

        @SerialName("other")
        Other
    }

    enum class Function {
        @SerialName("ENVOI")
        ENVOI,

        @SerialName("SUITE")
        SUITE,

        @SerialName("RETOUR")
        RETOUR,

        @SerialName("ANNULATION")
        ANNULATION,

        @SerialName("CORRECTION")
        CORRECTION,

        @SerialName("GUIDE")
        GUIDE,

        @SerialName("REPETITION")
        REPETITION,

        @SerialName("SOMMAIRE")
        SOMMAIRE,

        @SerialName("CNX")
        CNX,

        @SerialName("FIN")
        FIN,

        @SerialName("DIRECT")
        DIRECT,

        @SerialName("DIRECTCNX")
        DIRECTCNX,

        @SerialName("DIRECTCALLFAILED")
        DIRECTCALLFAILED,

        @SerialName("DIRECTCALLENDED")
        DIRECTCALLENDED,

        @SerialName("BGCALL")
        BGCALL,

        @SerialName("BGCALL_SIMU")
        BGCALL_SIMU,
    }
}
