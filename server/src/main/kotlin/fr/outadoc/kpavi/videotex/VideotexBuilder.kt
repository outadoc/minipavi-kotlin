package fr.outadoc.kpavi.videotex

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
        append(VideotextConstants.VDT_CRLF)
    }

    /**
     * Positions the cursor at the specified column and line.
     *
     * @param col Column number (1 to 40)
     * @param line Line number (0 to 24)
     * @return The command to position the cursor
     */
    fun moveCursorTo(col: Int, line: Int) {
        append(VideotextConstants.VDT_POS)
        append((64 + line).toChar())
        append((64 + col).toChar())
    }

    fun moveCursorRelative(direction: CursorDirection) {
        append(direction.code)
    }

    fun clearScreen() {
        append(VideotextConstants.VDT_CLR)
    }

    fun clearStatus() {
        moveCursorTo(1, 0)
        append(' ')
        repeatChar(' ', 39)
        moveCursorTo(1, 1)
    }

    fun clearAll() {
        clearScreen()
        clearStatus()
        showCursor(false)
    }

    fun clearLine() {
        append(VideotextConstants.VDT_CLRLN)
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
        append(VideotextConstants.VDT_REP)
        append((63 + num).toChar())
    }

    fun withTextColor(
        color: TextColor,
        block: VideotexBuilder.() -> Unit
    ) {
        append(color.code)
        block()
        append(VideotextConstants.VDT_TXTWHITE)
    }

    fun withBackgroundColor(
        color: BackgroundColor,
        block: VideotexBuilder.() -> Unit
    ) {
        append(color.code)
        block()
        append(VideotextConstants.VDT_FDNORM)
    }

    fun withBlink(block: VideotexBuilder.() -> Unit) {
        append(VideotextConstants.VDT_BLINK)
        block()
        append(VideotextConstants.VDT_FIXED)
    }

    fun withUnderline(block: VideotexBuilder.() -> Unit) {
        append(VideotextConstants.VDT_STARTUNDERLINE)
        block()
        append(VideotextConstants.VDT_STOPUNDERLINE)
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

    fun withCharacterSize(
        size: CharacterSize,
        block: VideotexBuilder.() -> Unit
    ) {
        append(size.code)
        block()
        append(CharacterSize.NORMAL.code)
    }

    fun showCursor(show: Boolean) {
        append(
            if (show) {
                VideotextConstants.VDT_CURON
            } else {
                VideotextConstants.VDT_CUROFF
            }
        )
    }

    fun setLocalEcho(enabled: Boolean) {
        append(
            if (enabled) {
                VideotextConstants.PRO_LOCALECHO_ON
            } else {
                VideotextConstants.PRO_LOCALECHO_OFF
            }
        )
    }

    fun build(): ByteString {
        return sb.toString().encodeToByteString()
    }

    companion object {

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
            'é' to "${VideotextConstants.VDT_G2}\u0042e",
            'è' to "${VideotextConstants.VDT_G2}\u0041e",
            'à' to "${VideotextConstants.VDT_G2}\u0041a",
            'ç' to "${VideotextConstants.VDT_G2}\u004B\u0063",
            'ê' to "${VideotextConstants.VDT_G2}\u0043e",
            'É' to "${VideotextConstants.VDT_G2}\u0042E",
            'È' to "${VideotextConstants.VDT_G2}\u0041E",
            'À' to "${VideotextConstants.VDT_G2}\u0041A",
            'Ç' to "${VideotextConstants.VDT_G2}\u004B\u0063",
            'Ê' to "${VideotextConstants.VDT_G2}\u0043E",
            'β' to "${VideotextConstants.VDT_G2}\u007B",
            'ß' to "${VideotextConstants.VDT_G2}\u007B",
            'œ' to "${VideotextConstants.VDT_G2}\u007A",
            'Œ' to "${VideotextConstants.VDT_G2}\u006A",
            'ü' to "${VideotextConstants.VDT_G2}\u0048\u0075",
            'û' to "${VideotextConstants.VDT_G2}\u0043\u0075",
            'ú' to "${VideotextConstants.VDT_G2}\u0042\u0075",
            'ù' to "${VideotextConstants.VDT_G2}\u0041\u0075",
            'ö' to "${VideotextConstants.VDT_G2}\u0048\u006F",
            'ô' to "${VideotextConstants.VDT_G2}\u0043\u006F",
            'ó' to "${VideotextConstants.VDT_G2}\u0042\u006F",
            'ò' to "${VideotextConstants.VDT_G2}\u0041\u006F",
            'ï' to "${VideotextConstants.VDT_G2}\u0048\u0069",
            'î' to "${VideotextConstants.VDT_G2}\u0043\u0069",
            'í' to "${VideotextConstants.VDT_G2}\u0042\u0069",
            'ì' to "${VideotextConstants.VDT_G2}\u0041\u0069",
            'ë' to "${VideotextConstants.VDT_G2}\u0048\u0065",
            'ä' to "${VideotextConstants.VDT_G2}\u0048\u0061",
            'â' to "${VideotextConstants.VDT_G2}\u0043\u0061",
            'á' to "${VideotextConstants.VDT_G2}\u0042\u0061",
            '£' to "${VideotextConstants.VDT_G2}\u0023",
            '°' to "${VideotextConstants.VDT_G2}\u0030",
            '±' to "${VideotextConstants.VDT_G2}\u0031",
            '←' to "${VideotextConstants.VDT_G2}\u002C",
            '↑' to "${VideotextConstants.VDT_G2}\u002D",
            '→' to "${VideotextConstants.VDT_G2}\u002E",
            '↓' to "${VideotextConstants.VDT_G2}\u002F",
            '¼' to "${VideotextConstants.VDT_G2}\u003C",
            '½' to "${VideotextConstants.VDT_G2}\u003D",
            '¾' to "${VideotextConstants.VDT_G2}\u003E",
            'Â' to "${VideotextConstants.VDT_G2}\u0043A",
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
    }
}
