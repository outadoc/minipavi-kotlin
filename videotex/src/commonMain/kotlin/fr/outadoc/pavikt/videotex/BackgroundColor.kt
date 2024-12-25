package fr.outadoc.pavikt.videotex

import kotlinx.io.bytestring.ByteString

public enum class BackgroundColor(
    internal val code: ByteString
) {
    BLACK(VdtConstants.VDT_BGBLACK),
    RED(VdtConstants.VDT_BGRED),
    GREEN(VdtConstants.VDT_BGGREEN),
    YELLOW(VdtConstants.VDT_BGYELLOW),
    BLUE(VdtConstants.VDT_BGBLUE),
    MAGENTA(VdtConstants.VDT_BGMAGENTA),
    CYAN(VdtConstants.VDT_BGCYAN),
    WHITE(VdtConstants.VDT_BGWHITE)
}
