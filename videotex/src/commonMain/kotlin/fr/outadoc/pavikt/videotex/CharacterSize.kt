package fr.outadoc.pavikt.videotex

import kotlinx.io.bytestring.ByteString

public enum class CharacterSize(
    internal val code: ByteString
) {
    NORMAL(VdtConstants.VDT_SZNORM),
    DOUBLE_WIDTH(VdtConstants.VDT_SZDBLW),
    DOUBLE_HEIGHT(VdtConstants.VDT_SZDBLH),
    DOUBLE_SIZE(VdtConstants.VDT_SZDBLHW)
}
