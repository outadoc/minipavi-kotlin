package fr.outadoc.minipavi.videotex

import kotlinx.io.bytestring.ByteString

public enum class Color(
    internal val textColorCode: ByteString,
    internal val backgroundColorCode: ByteString,
) {
    Black(
        textColorCode = VdtConstants.VDT_TXTBLACK,
        backgroundColorCode = VdtConstants.VDT_BGBLACK
    ),
    Red(
        textColorCode = VdtConstants.VDT_TXTRED,
        backgroundColorCode = VdtConstants.VDT_BGRED
    ),
    Green(
        textColorCode = VdtConstants.VDT_TXTGREEN,
        backgroundColorCode = VdtConstants.VDT_BGGREEN
    ),
    Yellow(
        textColorCode = VdtConstants.VDT_TXTYELLOW,
        backgroundColorCode = VdtConstants.VDT_BGYELLOW
    ),
    Blue(
        textColorCode = VdtConstants.VDT_TXTBLUE,
        backgroundColorCode = VdtConstants.VDT_BGBLUE
    ),
    Magenta(
        textColorCode = VdtConstants.VDT_TXTMAGENTA,
        backgroundColorCode = VdtConstants.VDT_BGMAGENTA
    ),
    Cyan(
        textColorCode = VdtConstants.VDT_TXTCYAN,
        backgroundColorCode = VdtConstants.VDT_BGCYAN
    ),
    White(
        textColorCode = VdtConstants.VDT_TXTWHITE,
        backgroundColorCode = VdtConstants.VDT_BGWHITE
    )
}
