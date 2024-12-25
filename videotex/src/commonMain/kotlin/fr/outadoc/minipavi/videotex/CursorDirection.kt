package fr.outadoc.minipavi.videotex

public enum class CursorDirection(
    internal val code: Byte
) {
    Left(VdtConstants.VDT_LEFT),
    Right(VdtConstants.VDT_RIGHT),
    Down(VdtConstants.VDT_DOWN),
    Up(VdtConstants.VDT_UP),
    LineStart(VdtConstants.VDT_CR),
}
