package fr.outadoc.pavikt.videotex

public enum class BackgroundColor(
    internal val code: String
) {
    BLACK(VideotextConstants.VDT_BGBLACK),
    RED(VideotextConstants.VDT_BGRED),
    GREEN(VideotextConstants.VDT_BGGREEN),
    YELLOW(VideotextConstants.VDT_BGYELLOW),
    BLUE(VideotextConstants.VDT_BGBLUE),
    MAGENTA(VideotextConstants.VDT_BGMAGENTA),
    CYAN(VideotextConstants.VDT_BGCYAN),
    WHITE(VideotextConstants.VDT_BGWHITE)
}
