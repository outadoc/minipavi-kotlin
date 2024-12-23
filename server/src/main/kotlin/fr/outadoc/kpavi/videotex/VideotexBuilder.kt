package fr.outadoc.kpavi.videotex

import fr.outadoc.kpavi.videotex.VideotextConstants.VDT_BLINK
import fr.outadoc.kpavi.videotex.VideotextConstants.VDT_CLR
import fr.outadoc.kpavi.videotex.VideotextConstants.VDT_CLRLN
import fr.outadoc.kpavi.videotex.VideotextConstants.VDT_CRLF
import fr.outadoc.kpavi.videotex.VideotextConstants.VDT_CUROFF
import fr.outadoc.kpavi.videotex.VideotextConstants.VDT_FDNORM
import fr.outadoc.kpavi.videotex.VideotextConstants.VDT_FIXED
import fr.outadoc.kpavi.videotex.VideotextConstants.VDT_G2
import fr.outadoc.kpavi.videotex.VideotextConstants.VDT_POS
import fr.outadoc.kpavi.videotex.VideotextConstants.VDT_REP
import fr.outadoc.kpavi.videotex.VideotextConstants.VDT_STARTUNDERLINE
import fr.outadoc.kpavi.videotex.VideotextConstants.VDT_STOPUNDERLINE
import fr.outadoc.kpavi.videotex.VideotextConstants.VDT_TXTWHITE
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
        sb.append(text.toG2())
    }

    fun append(char: Char) {
        append(char.toString())
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
    fun setPosition(col: Int, line: Int) {
        append(VDT_POS)
        append((64 + line).toChar())
        append((64 + col).toChar())
    }

    /**
     * Clears the entire screen.
     *
     * @return The command to clear the screen
     */
    fun clearScreen() {
        setPosition(1, 0)
        append(' ')
        repeatChar(' ', 39)
        setPosition(1, 1)
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
     * Generates a Videotex command to display a YouTube video on WebMedia.
     *
     * @param youtubeId The YouTube video ID
     * @return The command to display the YouTube video
     */
    fun webMediaYoutube(youtubeId: String) {
        setPosition(1, 0)
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
        setPosition(1, 0)
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
        setPosition(1, 0)
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
        setPosition(1, 0)
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
        setPosition(1, 0)
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
    private fun String.toG2(): String {
        return this
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

    fun withTextColor(
        color: TextColor,
        block: VideotexBuilder.() -> Unit
    ) {
        append(color.code)
        block()
        append(VDT_TXTWHITE)
    }

    fun withBackgroundColor(
        color: BackgroundColor,
        block: VideotexBuilder.() -> Unit
    ) {
        append(color.code)
        block()
        append(VDT_FDNORM)
    }

    fun withBlink(block: VideotexBuilder.() -> Unit) {
        append(VDT_BLINK)
        block()
        append(VDT_FIXED)
    }

    fun withUnderline(block: VideotexBuilder.() -> Unit) {
        append(VDT_STARTUNDERLINE)
        block()
        append(VDT_STOPUNDERLINE)
    }

    fun withRouleau(block: VideotexBuilder.() -> Unit) {
        append(VideotextConstants.PRO_ROULEAU_ON)
        block()
        append(VideotextConstants.PRO_ROULEAU_OFF)
    }

    fun withInvertedBackground(block: VideotexBuilder.() -> Unit) {
        append(VideotextConstants.VDT_FDINV)
        block()
        append(VideotextConstants.VDT_FDNORM)
    }
}
