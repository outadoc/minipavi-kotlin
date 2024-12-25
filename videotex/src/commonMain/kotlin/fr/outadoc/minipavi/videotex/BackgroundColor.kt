package fr.outadoc.minipavi.videotex

import kotlinx.io.bytestring.ByteString

public enum class BackgroundColor(
    internal val code: ByteString
) {
    Black(VdtConstants.VDT_BGBLACK),
    Red(VdtConstants.VDT_BGRED),
    Green(VdtConstants.VDT_BGGREEN),
    Yellow(VdtConstants.VDT_BGYELLOW),
    Blue(VdtConstants.VDT_BGBLUE),
    Magenta(VdtConstants.VDT_BGMAGENTA),
    Cyan(VdtConstants.VDT_BGCYAN),
    White(VdtConstants.VDT_BGWHITE),
}
