package fr.outadoc.minipavi.videotex

import kotlinx.io.bytestring.ByteString
import kotlinx.io.bytestring.ByteStringBuilder
import kotlinx.io.bytestring.append

/**
 * Permet de construire facilement un document Vidéotex.
 */
public fun buildVideotex(block: VideotexBuilder.() -> Unit): ByteString {
    return VideotexBuilder()
        .apply { block() }
        .build()
}

/**
 * Builder permettant de construire facilement un document Vidéotex.
 *
 * Utilisez les méthodes de cette classe pour ajouter du texte, des commandes,
 * des styles, etc. au document, puis appelez [build] pour obtenir le document finalisé.
 */
public class VideotexBuilder internal constructor() {

    private val bs = ByteStringBuilder()

    /**
     * La hauteur de l'écran (nombre de lignes).
     */
    public val screenHeight: Int = 25

    /**
     * La largeur de l'écran (nombre de colonnes).
     */
    public val screenWidth: Int = 40

    /**
     * Ajoute le texte spécifié au document, en convertissant
     * les caractères spéciaux si nécessaire.
     */
    public fun append(text: String) {
        bs.appendNormalizedText(text)
    }

    /**
     * Ajoute un caractère au document.
     */
    public fun append(char: Char) {
        append(char.toString())
    }

    /**
     * Ajoute le texte spécifié au document, en terminant par un retour
     * à la ligne et en convertissant les caractères spéciaux si nécessaire.
     */
    public fun appendLine(text: String = "") {
        append(text)
        bs.append(VdtConstants.VDT_CRLF)
    }

    /**
     * Ajoute un flux Vidéotex brut au document.
     */
    public fun appendRawVideotex(bytes: ByteString) {
        bs.append(bytes)
    }

    /**
     * Positionne le curseur aux coordonnées spécifiées.
     *
     * @param col Colonne (1 à 40)
     * @param line Ligne (0 à 24)
     *
     * @throws IllegalArgumentException Si les coordonnées sont invalides.
     */
    public fun moveCursorTo(col: Int, line: Int) {
        check(col in 1..40) { "Column must be between 1 and 40" }
        check(line in 0..24) { "Line must be between 0 and 24" }

        bs.append(VdtConstants.VDT_POS)
        bs.append((64 + line).toByte())
        bs.append((64 + col).toByte())
    }

    /**
     * Déplace le curseur dans la direction spécifiée.
     */
    public fun moveCursorRelative(direction: CursorDirection) {
        bs.append(direction.code)
    }

    /**
     * Efface l'écran.
     */
    public fun clearScreen() {
        bs.append(VdtConstants.VDT_CLR)
    }

    /**
     * Efface la ligne de statut.
     */
    public fun clearStatus() {
        moveCursorTo(1, 0)
        append(' ')
        repeatChar(' ', 39)
        moveCursorTo(1, 1)
    }

    /**
     * Efface l'écran, la ligne de statut, et masque le curseur.
     */
    public fun clearAll() {
        clearScreen()
        clearStatus()
        showCursor(false)
    }

    /**
     * Efface la ligne courante.
     */
    public fun clearLine() {
        bs.append(VdtConstants.VDT_CLRLN)
    }

    /**
     * Réinitialise les jeux de caractères.
     */
    public fun resetCharacterSets() {
        bs.append(VdtConstants.VDT_RESET_G0_CHARSET)
        bs.append(VdtConstants.VDT_RESET_G1_CHARSET)
    }

    /**
     * Répète un caractère un nombre spécifié de fois.
     *
     * @param char Caractère à répéter
     * @param repeatCount Nombre de répétitions, entre 1 et 63
     */
    public fun repeatChar(char: Char, repeatCount: Int) {
        check(repeatCount in 1..63) {
            "Cannot repeat character $char $repeatCount times: exceeds maximum value"
        }

        append(char)
        bs.append(VdtConstants.VDT_REP)
        bs.append((63 + repeatCount).toByte())
    }

    /**
     * Applique une couleur de texte au bloc spécifié.
     */
    public fun withTextColor(
        color: Color,
        block: VideotexBuilder.() -> Unit
    ) {
        bs.append(color.textColorCode)
        block()
        bs.append(VdtConstants.VDT_TXTWHITE)
    }

    /**
     * Applique une couleur de fond au bloc spécifié.
     */
    public fun withBackgroundColor(
        color: Color,
        block: VideotexBuilder.() -> Unit
    ) {
        bs.append(color.backgroundColorCode)
        block()
        bs.append(VdtConstants.VDT_BGTRANS)
    }

    /**
     * Applique un clignotement au bloc spécifié.
     */
    public fun withBlink(block: VideotexBuilder.() -> Unit) {
        bs.append(VdtConstants.VDT_BLINK)
        block()
        bs.append(VdtConstants.VDT_FIXED)
    }

    /**
     * Applique un soulignement au bloc spécifié.
     */
    public fun withUnderline(block: VideotexBuilder.() -> Unit) {
        bs.append(VdtConstants.VDT_STARTUNDERLINE)
        block()
        bs.append(VdtConstants.VDT_STOPUNDERLINE)
    }

    /**
     * Applique un mode rouleau au bloc spécifié.
     */
    public fun withRouleau(block: VideotexBuilder.() -> Unit) {
        bs.append(VdtConstants.PRO_ROULEAU_ON)
        block()
        bs.append(VdtConstants.PRO_ROULEAU_OFF)
    }

    /**
     * Inverse le fond du bloc spécifié.
     */
    public fun withInvertedBackground(block: VideotexBuilder.() -> Unit) {
        bs.append(VdtConstants.VDT_FDINV)
        block()
        bs.append(VdtConstants.VDT_FDNORM)
    }

    /**
     * Applique une taille de caractères au bloc spécifié.
     */
    public fun withCharacterSize(
        size: CharacterSize,
        block: VideotexBuilder.() -> Unit
    ) {
        bs.append(size.code)
        block()
        bs.append(CharacterSize.Normal.code)
    }

    /**
     * Affiche ou masque le curseur.
     */
    public fun showCursor(show: Boolean) {
        bs.append(
            if (show) {
                VdtConstants.VDT_CURON
            } else {
                VdtConstants.VDT_CUROFF
            }
        )
    }

    /**
     * Active ou désactive l'écho local.
     */
    public fun setLocalEcho(enabled: Boolean) {
        bs.append(
            if (enabled) {
                VdtConstants.PRO_LOCALECHO_ON
            } else {
                VdtConstants.PRO_LOCALECHO_OFF
            }
        )
    }

    /**
     * Active le mode mosaïque pour le bloc spécifié.
     *
     * Le mode mosaïque est un mode graphique qui permet de dessiner des caractères
     * sur une grille de 2x3 caractères. Les caractères sont dessinés en utilisant
     * les octets de 0x20 à 0x7F.
     */
    public fun withMosaic(block: VideotexBuilder.() -> Unit) {
        bs.append(VdtConstants.VDT_G1)
        block()
        bs.append(VdtConstants.VDT_G0)
    }

    /**
     * Construit le document Vidéotex finalisé en tant que chaîne binaire.
     */
    public fun build(): ByteString {
        return bs.toByteString()
    }

    private companion object {

        fun ByteStringBuilder.appendNormalizedText(text: String) {
            text.forEach { char ->
                append(
                    g2Map.getOrElse(
                        key = char,
                        defaultValue = { ByteString(char.code.toByte()) }
                    )
                )
            }
        }

        val g2Map: Map<Char, ByteString> = mapOf(
            'é' to ByteString(VdtConstants.VDT_G2, 0x42, 0x65),
            'è' to ByteString(VdtConstants.VDT_G2, 0x41, 0x65),
            'à' to ByteString(VdtConstants.VDT_G2, 0x41, 0x61),
            'ç' to ByteString(VdtConstants.VDT_G2, 0x4B, 0x63),
            'ê' to ByteString(VdtConstants.VDT_G2, 0x43, 0x65),
            'É' to ByteString(VdtConstants.VDT_G2, 0x42, 0x45),
            'È' to ByteString(VdtConstants.VDT_G2, 0x41, 0x45),
            'À' to ByteString(VdtConstants.VDT_G2, 0x41, 0x41),
            'Ç' to ByteString(VdtConstants.VDT_G2, 0x4B, 0x63),
            'Ê' to ByteString(VdtConstants.VDT_G2, 0x43, 0x45),
            'β' to ByteString(VdtConstants.VDT_G2, 0x7B),
            'ß' to ByteString(VdtConstants.VDT_G2, 0x7B),
            'œ' to ByteString(VdtConstants.VDT_G2, 0x7A),
            'Œ' to ByteString(VdtConstants.VDT_G2, 0x6A),
            'ü' to ByteString(VdtConstants.VDT_G2, 0x48, 0x75),
            'û' to ByteString(VdtConstants.VDT_G2, 0x43, 0x75),
            'ú' to ByteString(VdtConstants.VDT_G2, 0x42, 0x75),
            'ù' to ByteString(VdtConstants.VDT_G2, 0x41, 0x75),
            'ö' to ByteString(VdtConstants.VDT_G2, 0x48, 0x6F),
            'ô' to ByteString(VdtConstants.VDT_G2, 0x43, 0x6F),
            'ó' to ByteString(VdtConstants.VDT_G2, 0x42, 0x6F),
            'ò' to ByteString(VdtConstants.VDT_G2, 0x41, 0x6F),
            'ï' to ByteString(VdtConstants.VDT_G2, 0x48, 0x69),
            'î' to ByteString(VdtConstants.VDT_G2, 0x43, 0x69),
            'í' to ByteString(VdtConstants.VDT_G2, 0x42, 0x69),
            'ì' to ByteString(VdtConstants.VDT_G2, 0x41, 0x69),
            'ë' to ByteString(VdtConstants.VDT_G2, 0x48, 0x65),
            'ä' to ByteString(VdtConstants.VDT_G2, 0x48, 0x61),
            'â' to ByteString(VdtConstants.VDT_G2, 0x43, 0x61),
            'á' to ByteString(VdtConstants.VDT_G2, 0x42, 0x61),
            '£' to ByteString(VdtConstants.VDT_G2, 0x23),
            '°' to ByteString(VdtConstants.VDT_G2, 0x30),
            '±' to ByteString(VdtConstants.VDT_G2, 0x31),
            '←' to ByteString(VdtConstants.VDT_G2, 0x2C),
            '↑' to ByteString(VdtConstants.VDT_G2, 0x2D),
            '→' to ByteString(VdtConstants.VDT_G2, 0x2E),
            '↓' to ByteString(VdtConstants.VDT_G2, 0x2F),
            '¼' to ByteString(VdtConstants.VDT_G2, 0x3C),
            '½' to ByteString(VdtConstants.VDT_G2, 0x3D),
            '¾' to ByteString(VdtConstants.VDT_G2, 0x3E),
            'Â' to ByteString(VdtConstants.VDT_G2, 0x43, 0x41),
            'Î' to ByteString(0x49),
            'ō' to ByteString(0x6F),
            'á' to ByteString(0x61),
            '’' to ByteString(0x27),
            '\u00A0' to ByteString(0x20),
            'ň' to ByteString(0x6E),
            'ć' to ByteString(0x63),
            'ř' to ByteString(0x72),
            'ý' to ByteString(0x79),
            'š' to ByteString(0x73),
            'í' to ByteString(0x69),
            'ą' to ByteString(0x61),
        )
    }
}
