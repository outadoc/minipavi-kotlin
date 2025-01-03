package fr.outadoc.minipavi.videotex

import kotlinx.io.bytestring.ByteString

internal object VdtConstants {

    /**
     * Déplacement curseur vers la gauche.
     */
    val VDT_LEFT: Byte = 0x08

    /**
     * Déplacement curseur vers la droite.
     */
    val VDT_RIGHT: Byte = 0x09

    /**
     * Déplacement curseur vers le bas.
     */
    val VDT_DOWN: Byte = 0x0A

    /**
     * Déplacement curseur vers le haut.
     */
    val VDT_UP: Byte = 0x0B

    /**
     * Retour charriot (début de ligne).
     */
    val VDT_CR: Byte = 0x0D

    /**
     * Retour charriot + déplacement vers le bas.
     */
    val VDT_CRLF: ByteString = ByteString(0x0D, 0x0A)

    /**
     * Effacement écran.
     */
    val VDT_CLR: Byte = 0x0C

    /**
     * Jeu de caractères G0 (standard).
     */
    val VDT_G0: Byte = 0x0F

    /**
     * Jeu de caractères G1 (mosaïque).
     */
    val VDT_G1: Byte = 0x0E

    /**
     * Jeu de caractères G2.
     */
    val VDT_G2: Byte = 0x19

    /**
     * Positionnement du curseur.
     */
    val VDT_POS: Byte = 0x1F

    /**
     * Répétition caractère.
     */
    val VDT_REP: Byte = 0x12

    /**
     * Curseur allumé.
     */
    val VDT_CURON: Byte = 0x11

    /**
     * Curseur éteint.
     */
    val VDT_CUROFF: Byte = 0x14

    /**
     * Effacement fin de ligne.
     */
    val VDT_CLRLN: Byte = 0x18

    /**
     * Taille des caractères normale.
     */
    val VDT_SZNORM: ByteString = ByteString(0x1B, 0x4C)

    /**
     * Taille des caractères double hauteur.
     */
    val VDT_SZDBLH: ByteString = ByteString(0x1B, 0x4D)

    /**
     * Taille des caractères double largeur.
     */
    val VDT_SZDBLW: ByteString = ByteString(0x1B, 0x4E)

    /**
     * Taille des caractères double hauteur + double largeur.
     */
    val VDT_SZDBLHW: ByteString = ByteString(0x1B, 0x4F)

    /**
     * Couleur texte noir.
     */
    val VDT_TXTBLACK: ByteString = ByteString(0x1B, 0x40)

    /**
     * Couleur texte rouge.
     */
    val VDT_TXTRED: ByteString = ByteString(0x1B, 0x41)

    /**
     * Couleur texte vert.
     */
    val VDT_TXTGREEN: ByteString = ByteString(0x1B, 0x42)

    /**
     * Couleur texte jaune.
     */
    val VDT_TXTYELLOW: ByteString = ByteString(0x1B, 0x43)

    /**
     * Couleur texte bleu.
     */
    val VDT_TXTBLUE: ByteString = ByteString(0x1B, 0x44)

    /**
     * Couleur texte magenta.
     */
    val VDT_TXTMAGENTA: ByteString = ByteString(0x1B, 0x45)

    /**
     * Couleur texte cyan.
     */
    val VDT_TXTCYAN: ByteString = ByteString(0x1B, 0x46)

    /**
     * Couleur texte blanc.
     */
    val VDT_TXTWHITE: ByteString = ByteString(0x1B, 0x47)

    /**
     * Couleur fond noir.
     */
    val VDT_BGBLACK: ByteString = ByteString(0x1B, 0x50)

    /**
     * Couleur fond rouge.
     */
    val VDT_BGRED: ByteString = ByteString(0x1B, 0x51)

    /**
     * Couleur fond vert.
     */
    val VDT_BGGREEN: ByteString = ByteString(0x1B, 0x52)

    /**
     * Couleur fond jaune.
     */
    val VDT_BGYELLOW: ByteString = ByteString(0x1B, 0x53)

    /**
     * Couleur fond bleu.
     */
    val VDT_BGBLUE: ByteString = ByteString(0x1B, 0x54)

    /**
     * Couleur fond magenta.
     */
    val VDT_BGMAGENTA: ByteString = ByteString(0x1B, 0x55)

    /**
     * Couleur fond cyan.
     */
    val VDT_BGCYAN: ByteString = ByteString(0x1B, 0x56)

    /**
     * Couleur fond blanc.
     */
    val VDT_BGWHITE: ByteString = ByteString(0x1B, 0x57)

    /**
     * Fond transparent.
     */
    val VDT_BGTRANS: ByteString = ByteString(0x1B, 0x5E)

    /**
     * Clignotement.
     */
    val VDT_BLINK: ByteString = ByteString(0x1B, 0x48)

    /**
     * Arrêt clignotement.
     */
    val VDT_FIXED: ByteString = ByteString(0x1B, 0x49)

    /**
     * Arrêt soulignement.
     */
    val VDT_STOPUNDERLINE: ByteString = ByteString(0x1B, 0x59)

    /**
     * Début soulignement.
     */
    val VDT_STARTUNDERLINE: ByteString = ByteString(0x1B, 0x5A)

    /**
     * Fond inversé.
     */
    val VDT_FDINV: ByteString = ByteString(0x1B, 0x5D)

    /**
     * Fond normal.
     */
    val VDT_FDNORM: ByteString = ByteString(0x1B, 0x5C)

    /**
     * Passage clavier en minuscules.
     */
    val PRO_MIN: ByteString = ByteString(0x1B, 0x3A, 0x69, 0x45)

    /**
     * Passage clavier en majuscules.
     */
    val PRO_MAJ: ByteString = ByteString(0x1B, 0x3A, 0x6A, 0x45)

    /**
     * Arrêt echo local.
     */
    val PRO_LOCALECHO_OFF: ByteString = ByteString(0x1B, 0x3B, 0x60, 0x58, 0x51)

    /**
     * Marche echo local.
     */
    val PRO_LOCALECHO_ON: ByteString = ByteString(0x1B, 0x3B, 0x61, 0x58, 0x51)

    /**
     * Mode rouleau actif.
     */
    val PRO_ROULEAU_ON: ByteString = ByteString(0x1B, 0x3A, 0x69, 0x43)

    /**
     * Mode rouleau inactif.
     */
    val PRO_ROULEAU_OFF: ByteString = ByteString(0x1B, 0x3A, 0x6A, 0x43)

    /**
     * Réinitialisation des jeux de caractères normaux dans G0.
     */
    val VDT_RESET_G0_CHARSET: ByteString = ByteString(0x1B, 0x28, 0x40)

    /**
     * Réinitialisation des jeux de caractères normaux dans G1.
     */
    val VDT_RESET_G1_CHARSET: ByteString = ByteString(0x1B, 0x29, 0x63)
}