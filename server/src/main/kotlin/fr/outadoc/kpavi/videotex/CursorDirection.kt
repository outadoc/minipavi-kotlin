package fr.outadoc.kpavi.videotex

enum class CursorDirection(val code: String) {
    LEFT(VideotextConstants.VDT_LEFT),
    RIGHT(VideotextConstants.VDT_RIGHT),
    DOWN(VideotextConstants.VDT_DOWN),
    UP(VideotextConstants.VDT_UP),
    LINE_START(VideotextConstants.VDT_CR),
}
