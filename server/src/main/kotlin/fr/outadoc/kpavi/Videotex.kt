package fr.outadoc.kpavi

object Videotex {

    /**
     * Move cursor left
     */
    const val VDT_LEFT = "\u0008"

    /**
     * Move cursor right
     */
    const val VDT_RIGHT = "\u0009"

    /**
     * Move cursor down
     */
    const val VDT_DOWN = "\u000A"

    /**
     * Move cursor up
     */
    const val VDT_UP = "\u000B"

    /**
     * Carriage return (start of line)
     */
    const val VDT_CR = "\u000D"

    /**
     * Carriage return + move down
     */
    const val VDT_CRLF = "\u000D\u000A"

    /**
     * Clear screen
     */
    const val VDT_CLR = "\u000C"

    /**
     * Character set G0
     */
    const val VDT_G0 = "\u000F"

    /**
     * Character set G1
     */
    const val VDT_G1 = "\u000E"

    /**
     * Character set G2
     */
    const val VDT_G2 = "\u0019"

    /**
     * Cursor positioning
     */
    const val VDT_POS = "\u001F"

    /**
     * Character repetition
     */
    const val VDT_REP = "\u0012"

    /**
     * Cursor on
     */
    const val VDT_CURON = "\u0011"

    /**
     * Cursor off
     */
    const val VDT_CUROFF = "\u0014"

    /**
     * Clear end of line
     */
    const val VDT_CLRLN = "\u0018"

    /**
     * Normal character size
     */
    const val VDT_SZNORM = "\u001B\u004C"

    /**
     * Double height character size
     */
    const val VDT_SZDBLH = "\u001B\u004D"

    /**
     * Double width character size
     */
    const val VDT_SZDBLW = "\u001B\u004E"

    /**
     * Double height + double width character size
     */
    const val VDT_SZDBLHW = "\u001B\u004F"

    /**
     * Text color black
     */
    const val VDT_TXTBLACK = "\u001B@"

    /**
     * Text color red
     */
    const val VDT_TXTRED = "\u001BA"

    /**
     * Text color green
     */
    const val VDT_TXTGREEN = "\u001BB"

    /**
     * Text color yellow
     */
    const val VDT_TXTYELLOW = "\u001BC"

    /**
     * Text color blue
     */
    const val VDT_TXTBLUE = "\u001BD"

    /**
     * Text color magenta
     */
    const val VDT_TXTMAGENTA = "\u001BE"

    /**
     * Text color cyan
     */
    const val VDT_TXTCYAN = "\u001BF"

    /**
     * Text color white
     */
    const val VDT_TXTWHITE = "\u001BG"

    /**
     * Background color black
     */
    const val VDT_BGBLACK = "\u001BP"

    /**
     * Background color red
     */
    const val VDT_BGRED = "\u001BQ"

    /**
     * Background color green
     */
    const val VDT_BGGREEN = "\u001BR"

    /**
     * Background color yellow
     */
    const val VDT_BGYELLOW = "\u001BS"

    /**
     * Background color blue
     */
    const val VDT_BGBLUE = "\u001BT"

    /**
     * Background color magenta
     */
    const val VDT_BGMAGENTA = "\u001BU"

    /**
     * Background color cyan
     */
    const val VDT_BGCYAN = "\u001BV"

    /**
     * Background color white
     */
    const val VDT_BGWHITE = "\u001BW"

    /**
     * Blinking text
     */
    const val VDT_BLINK = "\u001BH"

    /**
     * Stop blinking
     */
    const val VDT_FIXED = "\u001BI"

    /**
     * Stop underline
     */
    const val VDT_STOPUNDERLINE = "\u001BY"

    /**
     * Start underline
     */
    const val VDT_STARTUNDERLINE = "\u001BZ"

    /**
     * Normal background
     */
    const val VDT_FDNORM = "\u001B\\"

    /**
     * Inverted background
     */
    const val VDT_FDINV = "\u001B]"

    /**
     * Switch keyboard to lowercase
     */
    const val PRO_MIN = "\u001B\u003A\u0069\u0045"

    /**
     * Switch keyboard to uppercase
     */
    const val PRO_MAJ = "\u001B\u003A\u006A\u0045"

    /**
     * Turn off local echo
     */
    const val PRO_LOCALECHO_OFF = "\u001B\u003B\u0060\u0058\u0051"

    /**
     * Turn on local echo
     */
    const val PRO_LOCALECHO_ON = "\u001B\u003B\u0061\u0058\u0051"

    /**
     * Enable roller mode
     */
    const val PRO_ROULEAU_ON = "\u001B\u003A\u0069\u0043"

    /**
     * Disable roller mode
     */
    const val PRO_ROULEAU_OFF = "\u001B\u003A\u006A\u0043"

    /**
     * Reset character sets
     */
    const val VDT_RESET_DRCS = "\u001B\u0028\u0040\u001B\u0029\u0063"

    /**
     * Positions the cursor at the specified column and line.
     *
     * @param col Column number (1 to 40)
     * @param line Line number (0 to 24)
     * @return The command to position the cursor
     */
    fun setPos(col: Int, line: Int): String {
        return buildString {
            append(VDT_POS)
            append((64 + line).toChar())
            append((64 + col).toChar())
        }
    }

    /**
     * Writes text on line 0 and then returns the cursor to the current position.
     *
     * @param txt The text to write
     * @param blink Whether the text should blink
     * @return The command to write the text
     */
    fun writeLine0(txt: String, blink: Boolean = false): String {
        return buildString {
            append(setPos(1, 0))
            if (blink) {
                append(VDT_BLINK)
            }
            append(txt.toG2())
            append(VDT_FIXED)
            append(VDT_CLRLN)
            appendLine()
        }
    }

    /**
     * Clears the entire screen.
     *
     * @return The command to clear the screen
     */
    fun clearScreen(): String {
        return buildString {
            append(setPos(1, 0))
            append(' ')
            append(repeatChar(' ', 39))
            append(setPos(1, 1))
            append(VDT_CLR)
            append(VDT_CUROFF)
        }
    }

    /**
     * Repeats a character a specified number of times.
     *
     * @param char The character to repeat
     * @param num The number of times to repeat the character
     * @return The command to repeat the character
     */
    fun repeatChar(char: Char, num: Int): String {
        return buildString {
            append(char)
            append(VDT_REP)
            append((63 + num).toChar())
        }
    }

    /**
     * Writes centered text on a specified line, preceded by attributes.
     *
     * @param line The line number
     * @param text The text to write
     * @param attr The attributes to apply
     * @return The command to write the centered text
     */
    fun writeCentered(line: Int, text: String, attr: String = ""): String {
        val pos = if (text.length >= 40) {
            setPos(1, line)
        } else {
            setPos((40 - text.length) / 2, line)
        }

        return buildString {
            append(pos)
            append(attr)
            append(text.toG2())
        }
    }

    /**
     * Generates a Videotex command to display a YouTube video on WebMedia.
     *
     * @param youtubeId The YouTube video ID
     * @return The command to display the YouTube video
     */
    fun webMediaYoutube(youtubeId: String): String {
        return buildString {
            append(setPos(1, 0))
            append("\u001B@\u0014#DYT:$youtubeId\u0014#F")
            append(VDT_CLRLN)
            appendLine()
        }
    }

    /**
     * Generates a Videotex command to display a video from a URL on WebMedia.
     *
     * @param url The URL of the video
     * @return The command to display the video
     */
    fun webMediaVideo(url: String): String {
        return buildString {
            append(setPos(1, 0))
            append("\u001B@\u0014#DVID:$url\u0014#F")
            append(VDT_CLRLN)
            appendLine()
        }
    }

    /**
     * Generates a Videotex command to play a sound from a URL on WebMedia.
     *
     * @param url The URL of the sound
     * @return The command to play the sound
     */
    fun webMediaSound(url: String): String {
        return buildString {
            append(setPos(1, 0))
            append("\u001B@\u0014#DSND:$url\u0014#F")
            append(VDT_CLRLN)
            appendLine()
        }
    }

    /**
     * Generates a Videotex command to display an image from a URL on WebMedia.
     *
     * @param url The URL of the image
     * @return The command to display the image
     */
    fun webMediaImg(url: String): String {
        return buildString {
            append(setPos(1, 0))
            append("\u001B@\u0014#DIMG:$url\u0014#F")
            append(VDT_CLRLN)
            appendLine()
        }
    }

    /**
     * Generates a Videotex command to create a link to a URL on WebMedia.
     *
     * @param url The URL to link to
     * @return The command to create the link
     */
    fun webMediaUrl(url: String): String {
        return buildString {
            append(setPos(1, 0))
            append("\u001B@\u0014#DURL:$url\u0014#F")
            append(VDT_CLRLN)
            appendLine()
        }
    }

    /**
     * Converts special characters to their G2 equivalents.
     *
     * @receiver The string to convert
     * @return The converted string
     */
    fun String.toG2(): String {
        return replace(
            regex = Regex("[\\x00-\\x1F\\x81\\x8D\\x8F\\x90\\x9D]"),
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
}
