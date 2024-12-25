package fr.outadoc.pavikt.videotex

import kotlinx.io.bytestring.ByteString
import kotlinx.io.bytestring.ByteStringBuilder
import kotlinx.io.bytestring.append

public fun buildVideotex(block: VideotexBuilder.() -> Unit): ByteString {
    return VideotexBuilder()
        .apply { block() }
        .build()
}

public class VideotexBuilder internal constructor() {

    private val bs = ByteStringBuilder()

    public fun append(text: String) {
        bs.appendNormalizedText(text)
    }

    public fun append(char: Char) {
        append(char.toString())
    }

    public fun appendLine(text: String = "") {
        append(text)
        bs.append(VdtConstants.VDT_CRLF)
    }

    public fun appendRawVideotex(bytes: ByteString) {
        bs.append(bytes)
    }

    /**
     * Positions the cursor at the specified column and line.
     *
     * @param col Column number (1 to 40)
     * @param line Line number (0 to 24)
     * @return The command to position the cursor
     */
    public fun moveCursorTo(col: Int, line: Int) {
        check(col in 1..40) { "Column must be between 1 and 40" }
        check(line in 0..24) { "Line must be between 0 and 24" }

        bs.append(VdtConstants.VDT_POS)
        bs.append((64 + line).toByte())
        bs.append((64 + col).toByte())
    }

    public fun moveCursorRelative(direction: CursorDirection) {
        bs.append(direction.code)
    }

    public fun clearScreen() {
        bs.append(VdtConstants.VDT_CLR)
    }

    public fun clearStatus() {
        moveCursorTo(1, 0)
        append(' ')
        repeatChar(' ', 39)
        moveCursorTo(1, 1)
    }

    public fun clearAll() {
        clearScreen()
        clearStatus()
        showCursor(false)
    }

    public fun clearLine() {
        bs.append(VdtConstants.VDT_CLRLN)
    }

    public fun resetCharacterSets() {
        bs.append(VdtConstants.VDT_RESET_DRCS)
    }

    /**
     * Repeats a character a specified number of times.
     *
     * @param char The character to repeat
     * @param num The number of times to repeat the character
     * @return The command to repeat the character
     */
    public fun repeatChar(char: Char, num: Int) {
        check(63 + num < Byte.MAX_VALUE) {
            "Cannot repeat character $char $num times: exceeds maximum value"
        }

        append(char)
        bs.append(VdtConstants.VDT_REP)
        bs.append((63 + num).toByte())
    }

    public fun withTextColor(
        color: TextColor,
        block: VideotexBuilder.() -> Unit
    ) {
        bs.append(color.code)
        block()
        bs.append(VdtConstants.VDT_TXTWHITE)
    }

    public fun withBackgroundColor(
        color: BackgroundColor,
        block: VideotexBuilder.() -> Unit
    ) {
        bs.append(color.code)
        block()
        bs.append(VdtConstants.VDT_FDNORM)
    }

    public fun withBlink(block: VideotexBuilder.() -> Unit) {
        bs.append(VdtConstants.VDT_BLINK)
        block()
        bs.append(VdtConstants.VDT_FIXED)
    }

    public fun withUnderline(block: VideotexBuilder.() -> Unit) {
        bs.append(VdtConstants.VDT_STARTUNDERLINE)
        block()
        bs.append(VdtConstants.VDT_STOPUNDERLINE)
    }

    public fun withRouleau(block: VideotexBuilder.() -> Unit) {
        bs.append(VdtConstants.PRO_ROULEAU_ON)
        block()
        bs.append(VdtConstants.PRO_ROULEAU_OFF)
    }

    public fun withInvertedBackground(block: VideotexBuilder.() -> Unit) {
        bs.append(VdtConstants.VDT_FDINV)
        block()
        bs.append(VdtConstants.VDT_FDNORM)
    }

    public fun withCharacterSize(
        size: CharacterSize,
        block: VideotexBuilder.() -> Unit
    ) {
        bs.append(size.code)
        block()
        bs.append(CharacterSize.NORMAL.code)
    }

    public fun showCursor(show: Boolean) {
        bs.append(
            if (show) {
                VdtConstants.VDT_CURON
            } else {
                VdtConstants.VDT_CUROFF
            }
        )
    }

    public fun setLocalEcho(enabled: Boolean) {
        bs.append(
            if (enabled) {
                VdtConstants.PRO_LOCALECHO_ON
            } else {
                VdtConstants.PRO_LOCALECHO_OFF
            }
        )
    }

    public fun build(): ByteString {
        return bs.toByteString()
    }

    private companion object {

        /**
         * Appends the [text] to the [ByteStringBuilder], converting special characters
         * to their Videotex equivalent.
         */
        private fun ByteStringBuilder.appendNormalizedText(text: String) {
            text.forEach { char ->
                append(
                    g2Map.getOrDefault(
                        key = char,
                        defaultValue = ByteString(char.code.toByte())
                    )
                )
            }
        }

        private val g2Map: Map<Char, ByteString> = mapOf(
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
