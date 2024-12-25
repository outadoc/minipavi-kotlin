package fr.outadoc.minipavi.videotex

import kotlinx.io.bytestring.ByteString

public enum class TextColor(
    internal val code: ByteString
) {
    BLACK(VdtConstants.VDT_TXTBLACK),
    RED(VdtConstants.VDT_TXTRED),
    GREEN(VdtConstants.VDT_TXTGREEN),
    YELLOW(VdtConstants.VDT_TXTYELLOW),
    BLUE(VdtConstants.VDT_TXTBLUE),
    MAGENTA(VdtConstants.VDT_TXTMAGENTA),
    CYAN(VdtConstants.VDT_TXTCYAN),
    WHITE(VdtConstants.VDT_TXTWHITE)
}
