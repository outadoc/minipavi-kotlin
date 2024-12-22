package fr.outadoc.kpavi.api.data.model

import fr.outadoc.kpavi.api.data.serializer.Base64Serializer
import fr.outadoc.kpavi.api.data.serializer.FunctionKeySetSerializer
import fr.outadoc.kpavi.api.data.serializer.InstantUnixEpochSerializer
import kotlinx.datetime.Instant
import kotlinx.io.bytestring.ByteString
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
data class ServiceResponse(
    /**
     * Version du client.
     */
    @SerialName("version")
    val version: String,

    /**
     * Le contenu de la page videotex à afficher, encodée en Base 64
     */
    @SerialName("content")
    @Serializable(with = Base64Serializer::class)
    val content: ByteString,

    /**
     * Données libres qui seront renvoyées inchangées par la suite par la passerelle
     */
    @SerialName("context")
    val context: String,

    /**
     * Active l'echo par la passerelle des caractères tapés par l'utilisateur,
     * pour que l'utilisateur voie ce qu'il tape.
     *
     * Généralement, cette clé aura la valeur [Command.OnOff.ON].
     */
    @SerialName("echo")
    val echo: Command.OnOff,

    /**
     * Demande à la passerelle d'appeler immédiatement l'url indiquée par la clé
     * [next], sans attendre une action de l'utilisateur.
     *
     * Valeurs possibles : `no`, `yes`, `yes-cnx`.
     * Si la valeur est `yes`, l'appel au service aura la clé `fctn` à la valeur `DIRECT`.
     * Si la valeur est `yes-cnx`, l'appel au service aura la clé `fctn` à la valeur `DIRECTCNX`.
     */
    @SerialName("directcall")
    val directCall: DirectCallSetting,

    /**
     * Prochaine URL du service qui devra être appelée par la passerelle.
     */
    @SerialName("next")
    val next: String,

    /**
     * Commande particulière que doit gérer la passerelle
     * (saisie texte, saisie message, etc.).
     */
    @SerialName("COMMAND")
    val command: Command,
) {
    @Serializable
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
    @Serializable
    @SerialName("InputTxt")
    data class InputText(
        @SerialName("param")
        val params: Params,
    ) : Command {

        @Serializable
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
            val length: Int,

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
             * Si [OnOff.ON], le curseur sera visible, sinon [OnOff.OFF].
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
             * La valeur indiquée est la somme des valeurs des touches de fonctions possibles :
             * - [FunctionKey.SOMMAIRE]
             * - [FunctionKey.RETOUR]
             * - [FunctionKey.REPETITION]
             * - [FunctionKey.GUIDE]
             * - [FunctionKey.SUITE]
             * - [FunctionKey.ENVOI]
             */
            @SerialName("validwith")
            @Serializable(with = FunctionKeySetSerializer::class)
            val submitWith: Set<FunctionKey>,
        )
    }

    @Serializable
    @SerialName("InputMsg")
    data class InputMessage(
        @SerialName("param")
        val params: Params,
    ) : Command {

        @Serializable
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
            @SerialName("w")
            val width: Int,

            /**
             * Hauteur (nombre de lignes) de la zone de saisie.
             */
            @SerialName("h")
            val height: Int,

            /**
             * Caractère pour affichage du champ de saisie (` ` ou `.` généralement).
             */
            @SerialName("spacechar")
            val spaceChar: String,

            /**
             * Tableau contenant les valeurs de pré-remplissage de la zone de saisie.
             * Chaque élément du tableau représente une ligne.
             */
            @SerialName("prefill")
            val prefill: List<String>,

            /**
             * Si [OnOff.ON], le curseur sera visible, sinon [OnOff.OFF].
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
             * De même que les touches Suite et Retour gérées directement par la passerelle pour le changement de ligne dans la zone de saisie.
             *
             * La valeur indiquée est la somme des valeurs des touches de fonctions possibles :
             * - [FunctionKey.SOMMAIRE]
             * - [FunctionKey.REPETITION]
             * - [FunctionKey.GUIDE]
             * - [FunctionKey.ENVOI]
             */
            @SerialName("validwith")
            @Serializable(with = FunctionKeySetSerializer::class)
            val submitWith: Set<FunctionKey>,
        )
    }

    @Serializable
    @SerialName("InputForm")
    data class InputForm(
        @SerialName("param")
        val params: Params,
    ) : Command {

        @Serializable
        data class Params(
            /**
             * Tableau des positions de la colonne (1-40) des zones de saisie.
             */
            @SerialName("x")
            val x: List<Int>,

            /**
             * Tableau des positions de la ligne (1-25) des zones de saisie.
             */
            @SerialName("y")
            val y: List<Int>,

            /**
             * Tableau des longueurs des zones de saisie (1-40).
             */
            @SerialName("l")
            val length: List<Int>,

            /**
             *  Caractère pour affichage du champ de saisie (` ` ou `.` généralement).
             */
            @SerialName("spacechar")
            val spaceChar: String,

            /**
             * Tableau contenant les valeurs de pré-remplissage de chaque zones de saisie.
             *
             * Chaque élément du tableau représente une zone de saisie.
             */
            @SerialName("prefill")
            val prefill: List<String>,

            /**
             * Si [OnOff.ON], le curseur sera visible, sinon [OnOff.OFF].
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
             * De même que les touches Suite et Retour gérées directement par la passerelle pour le changement de ligne dans la zone de saisie.
             *
             * La valeur indiquée est la somme des valeurs des touches de fonctions possibles :
             * - [FunctionKey.SOMMAIRE]
             * - [FunctionKey.REPETITION]
             * - [FunctionKey.GUIDE]
             * - [FunctionKey.ENVOI]
             */
            @SerialName("validwith")
            @Serializable(with = FunctionKeySetSerializer::class)
            val submitWith: Set<FunctionKey>,
        )
    }

    /**
     * Demande à la passerelle de déconnecter l'utilisateur du service.
     *
     * L'utilisateur retourne alors au service par défaut (généralement, l'accueil de la passerelle).
     */
    @SerialName("libCnx")
    @Serializable
    data object Disconnect : Command

    /**
     * Demande à la passerelle de d'afficher un message en ligne `0` aux autres utilisateurs.
     */
    @Serializable
    @SerialName("PushServiceMsg")
    data class PushServiceMessage(
        @SerialName("param")
        val params: Params,
    ) : Command {

        @Serializable
        data class Params(
            /**
             * Tableau contenant la liste des identifiants uniques des
             * utilisateurs vers lesquels envoyer un message.
             */
            @SerialName("uniqueid")
            val uniqueIds: List<String>,

            /**
             * Tableau contenant les messages à envoyer à chaque utilisateur.
             * Le message peut donc être différent pour chacun d'entre eux.
             */
            @SerialName("message")
            val message: List<String>,
        )
    }

    /**
     * Demande à la passerelle d'effectuer un appel à une url à l'heure indiquée.
     */
    @Serializable
    @SerialName("BackgroundCall")
    data class BackgroundCall(
        @SerialName("param")
        val params: Params
    ) : Command {

        @Serializable
        data class Params(
            /**
             * Timestamp Unix de l'heure prévue de l'appel.
             */
            @SerialName("time")
            @Serializable(with = InstantUnixEpochSerializer::class)
            val sendAt: Instant,

            /**
             * Si `false`, l'appel sera effectué vers l'url indiquée en paramètres.
             * Cet appel devra être vu par le service comme indépendant de l'action d'un utilisateur.
             * La touche de fonction indiquée dans la clé `fctn` aura la valeur `BGCALL`.
             * En retour, le service ne pourra qu'envoyer une commande `PushServiceMsg`
             * à la passerelle.
             *
             * Si `true`, l'appel sera effectué vers l'URL qui a été indiquée dans la clé
             * `nexturl` de l'utilisateur, avec en contenu` saisie` la valeur du paramètre `url`
             * (ci-dessous).
             *
             * La touche de fonction indiquée dans la clé `fctn` aura la valeur `BGCALL-SIMU`.
             * Cet appel devra être vu par le service comme une action de l'utilisateur.
             * En retour, le service peut envoyer toutes commandes et tout contenu,
             * qui sera alors envoyé à l'utilisateur.
             */
            @SerialName("simulate")
            val simulate: Boolean,

            /**
             * Tableau contenant la liste des identifiants uniques des utilisateurs.
             *
             * Si [simulate] est `true`, l'URL appelée sera celle indiquée dans la clé
             * `nexturl` de l'utilisateur identifié.
             *
             * Dans tous les cas, l'identifiant unique sera indiqué dans la clé `uniqueId`
             * de l'appel de la passerelle vers le service.
             */
            @SerialName("uniqueid")
            val uniqueIds: List<String>,

            /**
             * - Si [simulate] est `false`, indique l'url qui doit être appelée.
             * - Si [simulate] est `true`, indique les données qui seront indiqué dans la clé `content` de l'appel de la passerelle au service.
             */
            @SerialName("url")
            val url: String,
        )
    }

    /**
     * Demande à la passerelle de connecter l'utilisateur à un service Minitel accessible par Websocket.
     *
     * En fin de connexion, l'URL indiquée dans la clé `nexturl` de la requête
     * sera appelée et la touche de fonction indiquée sera `DIRECTCALLENDED`
     * si la connexion s'est terminée normalement ou `DIRECTCALLFAILED`
     * si la connexion a échoué. L'utilisateur peut mettre fin à la connexion
     * par la séquence `***` + `Sommaire` ou par la touche `Connexion/fin`.
     */
    @Serializable
    @SerialName("connectToWs")
    data class ConnectToWebSocket(
        @SerialName("param")
        val params: Params
    ) : Command {

        @Serializable
        data class Params(
            /**
             * Clé d'autorisation d'utilisation de cette commande
             * (configurée au niveau de la passerelle).
             *
             * Si l'adresse du serveur est la même que l'adresse du script
             * demandant cette commande, n'importe quelle clé est acceptée.
             */
            @SerialName("key")
            val key: String,

            /**
             * Adresse et port (obligatoire) du serveur (Exemple : `mntl.joher.com:2018`).
             *
             * Dans le cas d'une websocket SSL (wss), l'adresse doit être précédée de `ssl://`.
             */
            @SerialName("host")
            val host: String,

            /**
             * Éventuel chemin d'accès. Par défaut `/`
             */
            @SerialName("path")
            val path: String? = null,

            /**
             * Éventuel protocole supplémentaire à utiliser.
             * Vide par défaut.
             */
            @SerialName("proto")
            val proto: String? = null,

            /**
             * - [OnOff.ON] : l'écho est activé et géré par la passerelle.
             * - [OnOff.OFF] : l'écho est géré directement par le serveur.
             */
            @SerialName("echo")
            val echo: OnOff,

            /**
             * - [Case.LOWER] : force le clavier de l'utilisateur en minuscules.
             * - [Case.UPPER] : force le clavier de l'utilisateur en majuscules.
             */
            @SerialName("case")
            val case: Case,
        )
    }

    /**
     * Demande à la passerelle de connecter l'utilisateur à un service Minitel accessible par Telnet.
     *
     * En fin de connexion, l'URL indiquée dans la clé `nexturl` de la requête
     * sera appelée et la touche de fonction indiquée sera `DIRECTCALLENDED`
     * si la connexion s'est terminée normalement ou `DIRECTCALLFAILED`
     * si la connexion a échoué. L'utilisateur peut mettre fin à la connexion
     * par la séquence `***` + `Sommaire` ou par la touche `Connexion/fin`.
     */
    @Serializable
    @SerialName("connectToTln")
    data class ConnectToTelnet(
        @SerialName("param")
        val params: Params
    ) : Command {

        @Serializable
        data class Params(
            /**
             * Clé d'autorisation d'utilisation de cette commande
             * (configurée au niveau de la passerelle).
             *
             * Si l'adresse du serveur est la même que l'adresse du script
             * demandant cette commande, n'importe quelle clé est acceptée.
             */
            @SerialName("key")
            val key: String,

            /**
             * Adresse et port (obligatoire) du serveur (Exemple : `mntl.joher.com:2018`).
             *
             * Dans le cas d'une websocket SSL (wss), l'adresse doit être précédée de `ssl://`.
             */
            @SerialName("host")
            val host: String,

            /**
             * - [OnOff.ON] : l'écho est activé et géré par la passerelle.
             * - [OnOff.OFF] : l'écho est géré directement par le serveur.
             */
            @SerialName("echo")
            val echo: OnOff,

            /**
             * - [Case.LOWER] : force le clavier de l'utilisateur en minuscules.
             * - [Case.UPPER] : force le clavier de l'utilisateur en majuscules.
             */
            @SerialName("case")
            val case: Case,

            /**
             * Séquence optionnelle à envoyer en tout début de connexion au serveur
             */
            @SerialName("startseq")
            val startSequence: String,
        )
    }

    /**
     * Demande à la passerelle de connecter l'utilisateur à un service Minitel
     * accessible par téléphone.
     *
     * En fin de connexion, l'URL indiquée dans la clé `nexturl` de la requête
     * sera appelée et la touche de fonction indiquée sera `DIRECTCALLENDED`
     * si la connexion s'est terminée normalement ou `DIRECTCALLFAILED`
     * si la connexion a échoué. L'utilisateur peut mettre fin à la connexion
     * par la séquence `***` + `Sommaire` ou par la touche `Connexion/fin`.
     */
    @Serializable
    @SerialName("connectToExt")
    data class ConnectToExt(
        @SerialName("param")
        val params: Params
    ) : Command {

        @Serializable
        data class Params(
            /**
             * Clé d'autorisation d'utilisation de cette commande
             * (configurée au niveau de la passerelle).
             */
            @SerialName("key")
            val key: String,

            /**
             * Numéro d'appel du serveur.
             */
            @SerialName("number")
            val telNumber: String,

            /**
             * Niveau minimal en réception (en décibels)
             * (Ex : -35)
             */
            @SerialName("RX")
            val rx: String,

            /**
             * Niveau du signal transmis (en décibels)
             * (Ex : -30)
             */
            @SerialName("TX")
            val tx: String
        )
    }

    /**
     * Demande à la passerelle de connecter l'utilisateur A au flux
     * transmis à un autre utilisateur B (l'utilisateur A voit
     * ce que voit l'utilisateur B).
     *
     * En fin de connexion, l'URL indiquée dans la clé `nexturl` de la requête
     * sera appelée et la touche de fonction indiquée sera `DIRECTCALLENDED`
     * si la connexion s'est terminée normalement ou `DIRECTCALLFAILED`
     * si la connexion a échoué. L'utilisateur peut mettre fin à la connexion
     * par la séquence `***` + `Sommaire` ou par la touche `Connexion/fin`.
     */
    @Serializable
    @SerialName("duplicateStream")
    data class DuplicateStream(
        @SerialName("param")
        val params: Params
    ) : Command {

        @Serializable
        data class Params(

            /**
             * Clé d'autorisation d'utilisation de cette commande
             * (configurée au niveau de la passerelle).
             */
            @SerialName("key")
            val key: String,

            /**
             * Identifiant unique de l'utilisateur dont le flux sortant doit être dupliqué.
             */
            @SerialName("uniqueid")
            val uniqueId: String
        )
    }

    enum class OnOff {
        @SerialName("on")
        ON,

        @SerialName("off")
        OFF
    }

    enum class Case {
        @SerialName("upper")
        UPPER,

        @SerialName("lower")
        LOWER
    }

    enum class FunctionKey(val value: Int) {
        SOMMAIRE(1),
        RETOUR(4),
        REPETITION(8),
        GUIDE(16),
        SUITE(64),
        ENVOI(128)
    }
}
