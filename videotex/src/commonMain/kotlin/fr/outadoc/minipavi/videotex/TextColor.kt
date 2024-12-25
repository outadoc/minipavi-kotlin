package fr.outadoc.minipavi.videotex

import kotlinx.io.bytestring.ByteString

public enum class TextColor(
    internal val code: ByteString
) {
    Black(VdtConstants.VDT_TXTBLACK),
    Red(VdtConstants.VDT_TXTRED),
    Green(VdtConstants.VDT_TXTGREEN),
    Yellow(VdtConstants.VDT_TXTYELLOW),
    Blue(VdtConstants.VDT_TXTBLUE),
    Magenta(VdtConstants.VDT_TXTMAGENTA),
    Cyan(VdtConstants.VDT_TXTCYAN),
    White(VdtConstants.VDT_TXTWHITE)
}
