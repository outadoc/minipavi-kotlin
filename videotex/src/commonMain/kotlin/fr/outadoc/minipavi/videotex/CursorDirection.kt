package fr.outadoc.minipavi.videotex

public enum class CursorDirection(
    internal val code: Byte
) {
    LEFT(VdtConstants.VDT_LEFT),
    RIGHT(VdtConstants.VDT_RIGHT),
    DOWN(VdtConstants.VDT_DOWN),
    UP(VdtConstants.VDT_UP),
    LINE_START(VdtConstants.VDT_CR),
}
