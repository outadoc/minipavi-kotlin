package fr.outadoc.kpavi.api.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
data class GatewayResponse(
    /**
     * Version du client.
     */
    @SerialName("version")
    val version: String,

    /**
     * Le contenu de la page videotex à afficher, encodée en Base 64
     */
    @SerialName("content")
    val content: String,

    /**
     * Données libres qui seront renvoyées inchangées par la suite par la passerelle
     */
    @SerialName("context")
    val context: String,

    /**
     * Active l'echo par la passerelle des caractères tapés par l'utilisateur,
     * pour que l'utilisateur voie ce qu'il tape.
     *
     * Valeurs possibles : `on` ou `off`.
     * Généralement, cette clé aura la valeur `on`.
     */
    @SerialName("echo")
    val echo: EchoSetting,

    /**
     * Prochaine URL du service qui devra être appelée par la passerelle.
     */
    @SerialName("next")
    val next: String,

    /**
     * Demande à la passerelle d'appeler immédiatement l'url indiquée par la clé
     * [next], sans attendre une action de l'utilisateur.
     *
     * Valeurs possible : `no`, `yes`, `yes-cnx`.
     * Si la valeur est `yes`, l'appel au service aura la clé `fctn` à la valeur `DIRECT`.
     * Si la valeur est `yes-cnx`, l'appel au service aura la clé `fctn` à la valeur `DIRECTCNX`.
     */
    @SerialName("directcall")
    val directCall: DirectCallSetting,

    /**
     * Commande particulière que doit gérer la passerelle (saisie texte, saisie message, etc.).
     */
    @SerialName("COMMAND")
    val command: Command,
) {
    enum class EchoSetting {
        @SerialName("on")
        ON,

        @SerialName("off")
        OFF
    }

    enum class DirectCallSetting {
        @SerialName("no")
        NO,

        @SerialName("yes")
        YES,

        @SerialName("yes-cnx")
        YES_CNX
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("name")
sealed interface Command {

    /**
     * Demande à la passerelle de gérer la saisie par l'utilisateur d'une seule ligne de saisie,
     * de longueur définie.
     *
     * Généralement utilisée pour la saisie d'un choix.
     */
    @SerialName("InputTxt")
    data class InputText(
        @SerialName("params")
        val params: Params,
    ) : Command {
        data class Params(
            /**
             * Position de la colonne (1-40) de la zone de saisie.
             */
            @SerialName("x")
            val x: Int,

            /**
             * Position de la ligne (1-25) de la zone de saisie.
             */
            @SerialName("y")
            val y: Int,

            /**
             * Longueur de la zone de saisie (1-40).
             */
            @SerialName("l")
            val l: Int,

            /**
             *  Si non vide, quel que soit la caractère tapé par l'utilisateur,
             *  ce caractère s'affichera (pour la saisie de mot de passe par exemple).
             */
            @SerialName("char")
            val char: String,

            /**
             *  Caractère pour affichage du champ de saisie (` ` ou `.` généralement).
             */
            @SerialName("spacechar")
            val spaceChar: String,

            /**
             * Valeur de pré-remplissage du champ de saisie.
             */
            @SerialName("prefill")
            val prefill: String,

            /**
             * Si `on`, le curseur sera visible, sinon `off`.
             */
            @SerialName("cursor")
            val cursor: OnOff,

            /**
             * Valeur indiquant les touches de fonctions possibles qui valideront la saisie
             * (par exemple la touche Envoi).
             *
             * Les touches Correction et Annulation ne peuvent être définies car
             * gérées directement par la passerelle pour la correction de la saisie
             * par l'utilisateur.
             *
             * La valeur indiquée est la somme des valeurs des touches de fonctions possibles.
             * cf. [FunctionKeys]
             */
            @SerialName("validwith")
            val validWith: Int,
        )

        object FunctionKeys {
            const val SOMMAIRE = 1
            const val RETOUR = 4
            const val REPETITION = 8
            const val GUIDE = 16
            const val SUITE = 64
            const val ENVOI = 128
        }
    }

    @SerialName("InputMsg")
    data object InputMessage : Command

    @SerialName("InputForm")
    data object InputForm : Command

    @SerialName("libCnx")
    data object Disconnect : Command

    @SerialName("PushServiceMsg")
    data object PushServiceMessage : Command

    @SerialName("BackgroundCall")
    data object BackgroundCall : Command

    @SerialName("connectToWs")
    data object ConnectToWebSocket : Command

    @SerialName("connectToTln")
    data object ConnectToTelnet : Command

    @SerialName("connectToExt")
    data object ConnectToExt : Command

    @SerialName("duplicateStream")
    data object DuplicateStream : Command

    enum class OnOff {
        @SerialName("on")
        ON,

        @SerialName("off")
        OFF
    }
}
