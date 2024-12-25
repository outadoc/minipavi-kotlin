package fr.outadoc.minipavi.videotex

import kotlinx.io.bytestring.ByteString

public enum class CharacterSize(
    internal val code: ByteString
) {
    Normal(VdtConstants.VDT_SZNORM),
    DoubleWidth(VdtConstants.VDT_SZDBLW),
    DoubleHeight(VdtConstants.VDT_SZDBLH),
    DoubleSize(VdtConstants.VDT_SZDBLHW)
}
