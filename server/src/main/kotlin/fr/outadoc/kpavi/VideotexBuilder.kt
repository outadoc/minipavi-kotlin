package fr.outadoc.kpavi

import kotlinx.io.bytestring.ByteString
import kotlinx.io.bytestring.encodeToByteString

fun buildVideotex(block: VideotexBuilder.() -> Unit): ByteString {
    return VideotexBuilder()
        .apply { block() }
        .build()
}

class VideotexBuilder internal constructor() {

    private val sb = StringBuilder()

    fun append(text: String) {
        sb.append(text)
    }

    fun append(char: Char) {
        sb.append(char)
    }

    fun appendLine(text: String = "") {
        append(text)
        append(VDT_CRLF)
    }

    /**
     * Positions the cursor at the specified column and line.
     *
     * @param col Column number (1 to 40)
     * @param line Line number (0 to 24)
     * @return The command to position the cursor
     */
    fun setPos(col: Int, line: Int) {
        append(VDT_POS)
        append((64 + line).toChar())
        append((64 + col).toChar())
    }

    /**
     * Writes text on line 0 and then returns the cursor to the current position.
     *
     * @param txt The text to write
     * @param blink Whether the text should blink
     * @return The command to write the text
     */
    fun writeLine0(txt: String, blink: Boolean = false) {
        setPos(1, 0)
        if (blink) {
            append(VDT_BLINK)
        }
        append(txt.toG2())
        append(VDT_FIXED)
        append(VDT_CLRLN)
        appendLine()
    }

    /**
     * Clears the entire screen.
     *
     * @return The command to clear the screen
     */
    fun clearScreen() {
        setPos(1, 0)
        append(' ')
        repeatChar(' ', 39)
        setPos(1, 1)
        append(VDT_CLR)
        append(VDT_CUROFF)
    }

    /**
     * Repeats a character a specified number of times.
     *
     * @param char The character to repeat
     * @param num The number of times to repeat the character
     * @return The command to repeat the character
     */
    fun repeatChar(char: Char, num: Int) {
        append(char)
        append(VDT_REP)
        append((63 + num).toChar())
    }

    /**
     * Writes centered text on a specified line, preceded by attributes.
     *
     * @param line The line number
     * @param text The text to write
     * @param attr The attributes to apply
     * @return The command to write the centered text
     */
    fun writeCentered(line: Int, text: String, attr: String = "") {
        if (text.length >= 40) {
            setPos(1, line)
        } else {
            setPos((40 - text.length) / 2, line)
        }

        append(attr)
        append(text.toG2())
    }

    /**
     * Generates a Videotex command to display a YouTube video on WebMedia.
     *
     * @param youtubeId The YouTube video ID
     * @return The command to display the YouTube video
     */
    fun webMediaYoutube(youtubeId: String) {
        setPos(1, 0)
        append("\u001B@\u0014#DYT:$youtubeId\u0014#F")
        append(VDT_CLRLN)
        appendLine()
    }

    /**
     * Generates a Videotex command to display a video from a URL on WebMedia.
     *
     * @param url The URL of the video
     * @return The command to display the video
     */
    fun webMediaVideo(url: String) {
        setPos(1, 0)
        append("\u001B@\u0014#DVID:$url\u0014#F")
        append(VDT_CLRLN)
        appendLine()
    }

    /**
     * Generates a Videotex command to play a sound from a URL on WebMedia.
     *
     * @param url The URL of the sound
     * @return The command to play the sound
     */
    fun webMediaSound(url: String) {
        setPos(1, 0)
        append("\u001B@\u0014#DSND:$url\u0014#F")
        append(VDT_CLRLN)
        appendLine()
    }

    /**
     * Generates a Videotex command to display an image from a URL on WebMedia.
     *
     * @param url The URL of the image
     * @return The command to display the image
     */
    fun webMediaImg(url: String) {
        setPos(1, 0)
        append("\u001B@\u0014#DIMG:$url\u0014#F")
        append(VDT_CLRLN)
        appendLine()
    }

    /**
     * Generates a Videotex command to create a link to a URL on WebMedia.
     *
     * @param url The URL to link to
     * @return The command to create the link
     */
    fun webMediaUrl(url: String) {
        setPos(1, 0)
        append("\u001B@\u0014#DURL:$url\u0014#F")
        append(VDT_CLRLN)
        appendLine()
    }

    fun build(): ByteString {
        return sb.toString().encodeToByteString()
    }

    /**
     * Converts special characters to their G2 equivalents.
     *
     * @receiver The string to convert
     * @return The converted string
     */
    fun String.toG2(): String {
        return replace(
            regex = Regex("[\\u0000-\\u001F\\u0081\\u008D\\u008F\\u0090\\u009D]"),
            replacement = " "
        )
            .map { char ->
                g2Map.getOrDefault(char, char.toString())
            }
            .joinToString("")
    }

    private val g2Map = mapOf(
        'é' to "${VDT_G2}\u0042e",
        'è' to "${VDT_G2}\u0041e",
        'à' to "${VDT_G2}\u0041a",
        'ç' to "${VDT_G2}\u004B\u0063",
        'ê' to "${VDT_G2}\u0043e",
        'É' to "${VDT_G2}\u0042E",
        'È' to "${VDT_G2}\u0041E",
        'À' to "${VDT_G2}\u0041A",
        'Ç' to "${VDT_G2}\u004B\u0063",
        'Ê' to "${VDT_G2}\u0043E",
        'β' to "${VDT_G2}\u007B",
        'ß' to "${VDT_G2}\u007B",
        'œ' to "${VDT_G2}\u007A",
        'Œ' to "${VDT_G2}\u006A",
        'ü' to "${VDT_G2}\u0048\u0075",
        'û' to "${VDT_G2}\u0043\u0075",
        'ú' to "${VDT_G2}\u0042\u0075",
        'ù' to "${VDT_G2}\u0041\u0075",
        'ö' to "${VDT_G2}\u0048\u006F",
        'ô' to "${VDT_G2}\u0043\u006F",
        'ó' to "${VDT_G2}\u0042\u006F",
        'ò' to "${VDT_G2}\u0041\u006F",
        'ï' to "${VDT_G2}\u0048\u0069",
        'î' to "${VDT_G2}\u0043\u0069",
        'í' to "${VDT_G2}\u0042\u0069",
        'ì' to "${VDT_G2}\u0041\u0069",
        'ë' to "${VDT_G2}\u0048\u0065",
        'ä' to "${VDT_G2}\u0048\u0061",
        'â' to "${VDT_G2}\u0043\u0061",
        'á' to "${VDT_G2}\u0042\u0061",
        '£' to "${VDT_G2}\u0023",
        '°' to "${VDT_G2}\u0030",
        '±' to "${VDT_G2}\u0031",
        '←' to "${VDT_G2}\u002C",
        '↑' to "${VDT_G2}\u002D",
        '→' to "${VDT_G2}\u002E",
        '↓' to "${VDT_G2}\u002F",
        '¼' to "${VDT_G2}\u003C",
        '½' to "${VDT_G2}\u003D",
        '¾' to "${VDT_G2}\u003E",
        'Â' to "${VDT_G2}\u0043A",
        'Î' to "I",
        'ō' to "o",
        'á' to "a",
        '’' to "'",
        '\u00A0' to " ",
        'ň' to "n",
        'ć' to "c",
        'ř' to "r",
        'ý' to "y",
        'š' to "s",
        'í' to "i",
        'ą' to "a"
    )

    companion object {

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
         * Taille des caratcères normale.
         */
        const val VDT_SZNORM = "\u001B\u004C"

        /**
         * Taille des caratcères double hauteur.
         */
        const val VDT_SZDBLH = "\u001B\u004D"

        /**
         * Taille des caratcères double largeur.
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
}
