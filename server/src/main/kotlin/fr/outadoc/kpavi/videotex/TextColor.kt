package fr.outadoc.kpavi.videotex

enum class TextColor(val code: String) {
    BLACK(VideotextConstants.VDT_TXTBLACK),
    RED(VideotextConstants.VDT_TXTRED),
    GREEN(VideotextConstants.VDT_TXTGREEN),
    YELLOW(VideotextConstants.VDT_TXTYELLOW),
    BLUE(VideotextConstants.VDT_TXTBLUE),
    MAGENTA(VideotextConstants.VDT_TXTMAGENTA),
    CYAN(VideotextConstants.VDT_TXTCYAN),
    WHITE(VideotextConstants.VDT_TXTWHITE)
}
