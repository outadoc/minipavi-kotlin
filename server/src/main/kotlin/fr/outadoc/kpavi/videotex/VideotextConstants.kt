package fr.outadoc.kpavi.videotex

object VideotextConstants {

    /**
     * Déplacement curseur vers la gauche.
     */
    const val VDT_LEFT = "\u0008"

    /**
     * Déplacement curseur vers la droite.
     */
    const val VDT_RIGHT = "\u0009"

    /**
     * Déplacement curseur vers le bas.
     */
    const val VDT_DOWN = "\u000A"

    /**
     * Déplacement curseur vers le haut.
     */
    const val VDT_UP = "\u000B"

    /**
     * Retour charriot (début de ligne).
     */
    const val VDT_CR = "\u000D"

    /**
     * Retour charriot + déplacement vers le bas.
     */
    const val VDT_CRLF = "\u000D\u000A"

    /**
     * Effacement écran.
     */
    const val VDT_CLR = "\u000C"

    /**
     * Jeu de caractères G0.
     */
    const val VDT_G0 = "\u000F"

    /**
     * Jeu de caractères G1.
     */
    const val VDT_G1 = "\u000E"

    /**
     * Jeu de caractères G2.
     */
    const val VDT_G2 = "\u0019"

    /**
     * Positionnement du curseur.
     */
    const val VDT_POS = "\u001F"

    /**
     * Répétition caractère.
     */
    const val VDT_REP = "\u0012"

    /**
     * Curseur allumé.
     */
    const val VDT_CURON = "\u0011"

    /**
     * Curseur éteint.
     */
    const val VDT_CUROFF = "\u0014"

    /**
     * Effacement fin de ligne.
     */
    const val VDT_CLRLN = "\u0018"

    /**
     * Taille des caractères normale.
     */
    const val VDT_SZNORM = "\u001B\u004C"

    /**
     * Taille des caractères double hauteur.
     */
    const val VDT_SZDBLH = "\u001B\u004D"

    /**
     * Taille des caractères double largeur.
     */
    const val VDT_SZDBLW = "\u001B\u004E"

    /**
     * Taille des caractères double hauteur + double largeur.
     */
    const val VDT_SZDBLHW = "\u001B\u004F"

    /**
     * Couleur texte noir.
     */
    const val VDT_TXTBLACK = "\u001B@"

    /**
     * Couleur texte rouge.
     */
    const val VDT_TXTRED = "\u001BA"

    /**
     * Couleur texte vert.
     */
    const val VDT_TXTGREEN = "\u001BB"

    /**
     * Couleur texte jaune.
     */
    const val VDT_TXTYELLOW = "\u001BC"

    /**
     * Couleur texte bleu.
     */
    const val VDT_TXTBLUE = "\u001BD"

    /**
     * Couleur texte magenta.
     */
    const val VDT_TXTMAGENTA = "\u001BE"

    /**
     * Couleur texte cyan.
     */
    const val VDT_TXTCYAN = "\u001BF"

    /**
     * Couleur texte blanc.
     */
    const val VDT_TXTWHITE = "\u001BG"

    /**
     * Couleur fond noir.
     */
    const val VDT_BGBLACK = "\u001BP"

    /**
     * Couleur fond rouge.
     */
    const val VDT_BGRED = "\u001BQ"

    /**
     * Couleur fond vert.
     */
    const val VDT_BGGREEN = "\u001BR"

    /**
     * Couleur fond jaune.
     */
    const val VDT_BGYELLOW = "\u001BS"

    /**
     * Couleur fond bleu.
     */
    const val VDT_BGBLUE = "\u001BT"

    /**
     * Couleur fond magenta.
     */
    const val VDT_BGMAGENTA = "\u001BU"

    /**
     * Couleur fond cyan.
     */
    const val VDT_BGCYAN = "\u001BV"

    /**
     * Couleur fond blanc.
     */
    const val VDT_BGWHITE = "\u001BW"

    /**
     * Clignotement.
     */
    const val VDT_BLINK = "\u001BH"

    /**
     * Arrêt clignotement.
     */
    const val VDT_FIXED = "\u001BI"

    /**
     * Arrêt soulignement.
     */
    const val VDT_STOPUNDERLINE = "\u001BY"

    /**
     * Début soulignement.
     */
    const val VDT_STARTUNDERLINE = "\u001BZ"

    /**
     * Fond normal.
     */
    const val VDT_FDNORM = "\u001B\\"

    /**
     * Fond inversé.
     */
    const val VDT_FDINV = "\u001B]"

    /**
     * Passage clavier en minuscules.
     */
    const val PRO_MIN = "\u001B\u003A\u0069\u0045"

    /**
     * Passage clavier en majuscules.
     */
    const val PRO_MAJ = "\u001B\u003A\u006A\u0045"

    /**
     * Arrêt echo local.
     */
    const val PRO_LOCALECHO_OFF = "\u001B\u003B\u0060\u0058\u0051"

    /**
     * Marche echo local.
     */
    const val PRO_LOCALECHO_ON = "\u001B\u003B\u0061\u0058\u0051"

    /**
     * Mode rouleau actif.
     */
    const val PRO_ROULEAU_ON = "\u001B\u003A\u0069\u0043"

    /**
     * Mode rouleau inactif.
     */
    const val PRO_ROULEAU_OFF = "\u001B\u003A\u006A\u0043"

    /**
     * "Réinitialisation" des jeux de caractères normaux.
     */
    const val VDT_RESET_DRCS = "\u001B\u0028\u0040\u001B\u0029\u0063"
}