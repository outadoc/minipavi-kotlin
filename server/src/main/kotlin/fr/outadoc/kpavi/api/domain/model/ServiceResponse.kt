package fr.outadoc.kpavi.api.domain.model

import kotlinx.datetime.Instant
import kotlinx.io.bytestring.ByteString
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonClassDiscriminator

data class ServiceResponse<T: Any>(
    /**
     * Version du client.
     */
    val version: String,

    /**
     * Le contenu de la page videotex à afficher, encodée en Base 64
     */
    val content: ByteString,

    /**
     * Données libres qui seront renvoyées inchangées par la suite par la passerelle
     */
    val context: T? = null,

    /**
     * Active l'echo par la passerelle des caractères tapés par l'utilisateur,
     * pour que l'utilisateur voie ce qu'il tape.
     *
     * Généralement, cette clé aura la valeur [Command.OnOff.ON].
     */
    val echo: Command.OnOff = Command.OnOff.ON,

    /**
     * Demande à la passerelle d'appeler immédiatement l'url indiquée par la clé
     * [next], sans attendre une action de l'utilisateur.
     *
     * - Si la valeur est `yes`, l'appel au service aura la clé `fctn` à la valeur `DIRECT`.
     * - Si la valeur est `yes-cnx`, l'appel au service aura la clé `fctn` à la valeur `DIRECTCNX`.
     */
    val directCall: DirectCallSetting = DirectCallSetting.NO,

    /**
     * Prochaine URL du service qui devra être appelée par la passerelle.
     */
    val next: String = "",

    /**
     * Commande particulière que doit gérer la passerelle
     * (saisie texte, saisie message, etc.).
     */
    val command: Command? = null,
) {
    enum class DirectCallSetting {
        NO,

        YES,

        YES_CNX
    }
}

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("name")
sealed interface Command {

    /**
     * Demande à la passerelle de gérer la saisie par l'utilisateur d'une seule ligne de saisie,
     * de longueur définie.
     *
     * Généralement utilisée pour la saisie d'un choix.
     */
    data class InputText(
        val params: Params,
    ) : Command {

        data class Params(
            /**
             * Position de la colonne (1-40) de la zone de saisie.
             */
            val x: Int,

            /**
             * Position de la ligne (1-25) de la zone de saisie.
             */
            val y: Int,

            /**
             * Longueur de la zone de saisie (1-40).
             */
            val length: Int,

            /**
             *  Si non vide, quel que soit la caractère tapé par l'utilisateur,
             *  ce caractère s'affichera (pour la saisie de mot de passe par exemple).
             */
            val char: String,

            /**
             *  Caractère pour affichage du champ de saisie (` ` ou `.` généralement).
             */
            val spaceChar: String,

            /**
             * Valeur de pré-remplissage du champ de saisie.
             */
            val prefill: String,

            /**
             * Si [OnOff.ON], le curseur sera visible, sinon [OnOff.OFF].
             */
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
            val submitWith: Set<FunctionKey>,
        )
    }

    data class InputMessage(
        val params: Params,
    ) : Command {

        data class Params(
            /**
             * Position de la colonne (1-40) de la zone de saisie.
             */
            val x: Int,

            /**
             * Position de la ligne (1-25) de la zone de saisie.
             */
            val y: Int,

            /**
             * Longueur de la zone de saisie (1-40).
             */
            val width: Int,

            /**
             * Hauteur (nombre de lignes) de la zone de saisie.
             */
            val height: Int,

            /**
             * Caractère pour affichage du champ de saisie (` ` ou `.` généralement).
             */
            val spaceChar: String,

            /**
             * Tableau contenant les valeurs de pré-remplissage de la zone de saisie.
             * Chaque élément du tableau représente une ligne.
             */
            val prefill: List<String>,

            /**
             * Si [OnOff.ON], le curseur sera visible, sinon [OnOff.OFF].
             */
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
            val submitWith: Set<FunctionKey>,
        )
    }

    data class InputForm(
        val params: Params,
    ) : Command {

        data class Params(
            /**
             * Tableau des positions de la colonne (1-40) des zones de saisie.
             */
            val x: List<Int>,

            /**
             * Tableau des positions de la ligne (1-25) des zones de saisie.
             */
            val y: List<Int>,

            /**
             * Tableau des longueurs des zones de saisie (1-40).
             */
            val length: List<Int>,

            /**
             *  Caractère pour affichage du champ de saisie (` ` ou `.` généralement).
             */
            val spaceChar: String,

            /**
             * Tableau contenant les valeurs de pré-remplissage de chaque zones de saisie.
             *
             * Chaque élément du tableau représente une zone de saisie.
             */
            val prefill: List<String>,

            /**
             * Si [OnOff.ON], le curseur sera visible, sinon [OnOff.OFF].
             */
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
            val submitWith: Set<FunctionKey>,
        )
    }

    /**
     * Demande à la passerelle de déconnecter l'utilisateur du service.
     *
     * L'utilisateur retourne alors au service par défaut (généralement, l'accueil de la passerelle).
     */
    data object Disconnect : Command

    /**
     * Demande à la passerelle de d'afficher un message en ligne `0` aux autres utilisateurs.
     */
    data class PushServiceMessage(
        val params: Params,
    ) : Command {

        data class Params(
            /**
             * Tableau contenant la liste des identifiants uniques des
             * utilisateurs vers lesquels envoyer un message.
             */
            val uniqueIds: List<String>,

            /**
             * Tableau contenant les messages à envoyer à chaque utilisateur.
             * Le message peut donc être différent pour chacun d'entre eux.
             */
            val message: List<String>,
        )
    }

    /**
     * Demande à la passerelle d'effectuer un appel à une url à l'heure indiquée.
     */
    data class BackgroundCall(
        val params: Params
    ) : Command {

        data class Params(
            /**
             * Timestamp Unix de l'heure prévue de l'appel.
             */
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
            val uniqueIds: List<String>,

            /**
             * - Si [simulate] est `false`, indique l'url qui doit être appelée.
             * - Si [simulate] est `true`, indique les données qui seront indiqué dans la clé `content` de l'appel de la passerelle au service.
             */
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
    data class ConnectToWebSocket(
        val params: Params
    ) : Command {

        data class Params(
            /**
             * Clé d'autorisation d'utilisation de cette commande
             * (configurée au niveau de la passerelle).
             *
             * Si l'adresse du serveur est la même que l'adresse du script
             * demandant cette commande, n'importe quelle clé est acceptée.
             */
            val key: String,

            /**
             * Adresse et port (obligatoire) du serveur (Exemple : `mntl.joher.com:2018`).
             *
             * Dans le cas d'une websocket SSL (wss), l'adresse doit être précédée de `ssl://`.
             */
            val host: String,

            /**
             * Éventuel chemin d'accès. Par défaut `/`
             */
            val path: String? = null,

            /**
             * Éventuel protocole supplémentaire à utiliser.
             * Vide par défaut.
             */
            val proto: String? = null,

            /**
             * - [OnOff.ON] : l'écho est activé et géré par la passerelle.
             * - [OnOff.OFF] : l'écho est géré directement par le serveur.
             */
            val echo: OnOff,

            /**
             * - [Case.LOWER] : force le clavier de l'utilisateur en minuscules.
             * - [Case.UPPER] : force le clavier de l'utilisateur en majuscules.
             */
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
    data class ConnectToTelnet(
        val params: Params
    ) : Command {

        data class Params(
            /**
             * Clé d'autorisation d'utilisation de cette commande
             * (configurée au niveau de la passerelle).
             *
             * Si l'adresse du serveur est la même que l'adresse du script
             * demandant cette commande, n'importe quelle clé est acceptée.
             */
            val key: String,

            /**
             * Adresse et port (obligatoire) du serveur (Exemple : `mntl.joher.com:2018`).
             *
             * Dans le cas d'une websocket SSL (wss), l'adresse doit être précédée de `ssl://`.
             */
            val host: String,

            /**
             * - [OnOff.ON] : l'écho est activé et géré par la passerelle.
             * - [OnOff.OFF] : l'écho est géré directement par le serveur.
             */
            val echo: OnOff,

            /**
             * - [Case.LOWER] : force le clavier de l'utilisateur en minuscules.
             * - [Case.UPPER] : force le clavier de l'utilisateur en majuscules.
             */
            val case: Case,

            /**
             * Séquence optionnelle à envoyer en tout début de connexion au serveur
             */
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
    data class ConnectToExt(
        val params: Params
    ) : Command {

        data class Params(
            /**
             * Clé d'autorisation d'utilisation de cette commande
             * (configurée au niveau de la passerelle).
             */
            val key: String,

            /**
             * Numéro d'appel du serveur.
             */
            val telNumber: String,

            /**
             * Niveau minimal en réception (en décibels)
             * (Ex : -35)
             */
            val rx: String,

            /**
             * Niveau du signal transmis (en décibels)
             * (Ex : -30)
             */
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
    data class DuplicateStream(
        val params: Params
    ) : Command {

        data class Params(

            /**
             * Clé d'autorisation d'utilisation de cette commande
             * (configurée au niveau de la passerelle).
             */
            val key: String,

            /**
             * Identifiant unique de l'utilisateur dont le flux sortant doit être dupliqué.
             */
            val uniqueId: String
        )
    }

    enum class OnOff {
        ON,
        OFF
    }

    enum class Case {
        UPPER,
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
