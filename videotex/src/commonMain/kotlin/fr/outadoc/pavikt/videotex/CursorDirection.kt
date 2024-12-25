package fr.outadoc.pavikt.videotex

public enum class CursorDirection(
    internal val code: String
) {
    LEFT(VideotextConstants.VDT_LEFT),
    RIGHT(VideotextConstants.VDT_RIGHT),
    DOWN(VideotextConstants.VDT_DOWN),
    UP(VideotextConstants.VDT_UP),
    LINE_START(VideotextConstants.VDT_CR),
}
