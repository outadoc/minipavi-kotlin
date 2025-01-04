package fr.outadoc.minipavi.videotex

import kotlinx.io.bytestring.ByteString

/**
 * Couleurs disponibles sur les terminaux la supportant.
 */
public enum class Color(
    internal val textColorCode: ByteString,
    internal val backgroundColorCode: ByteString,
) {
    /**
     * Noir.
     */
    Black(
        textColorCode = VdtConstants.VDT_TXTBLACK,
        backgroundColorCode = VdtConstants.VDT_BGBLACK
    ),

    /**
     * Rouge.
     */
    Red(
        textColorCode = VdtConstants.VDT_TXTRED,
        backgroundColorCode = VdtConstants.VDT_BGRED
    ),

    /**
     * Vert.
     */
    Green(
        textColorCode = VdtConstants.VDT_TXTGREEN,
        backgroundColorCode = VdtConstants.VDT_BGGREEN
    ),

    /**
     * Jaune.
     */
    Yellow(
        textColorCode = VdtConstants.VDT_TXTYELLOW,
        backgroundColorCode = VdtConstants.VDT_BGYELLOW
    ),

    /**
     * Bleu.
     */
    Blue(
        textColorCode = VdtConstants.VDT_TXTBLUE,
        backgroundColorCode = VdtConstants.VDT_BGBLUE
    ),

    /**
     * Magenta.
     */
    Magenta(
        textColorCode = VdtConstants.VDT_TXTMAGENTA,
        backgroundColorCode = VdtConstants.VDT_BGMAGENTA
    ),

    /**
     * Cyan.
     */
    Cyan(
        textColorCode = VdtConstants.VDT_TXTCYAN,
        backgroundColorCode = VdtConstants.VDT_BGCYAN
    ),

    /**
     * Blanc.
     */
    White(
        textColorCode = VdtConstants.VDT_TXTWHITE,
        backgroundColorCode = VdtConstants.VDT_BGWHITE
    )
}
