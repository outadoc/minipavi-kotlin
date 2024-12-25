package fr.outadoc.pavikt.videotex

public enum class CharacterSize(
    internal val code: String
) {
    NORMAL(VideotextConstants.VDT_SZNORM),
    DOUBLE_WIDTH(VideotextConstants.VDT_SZDBLW),
    DOUBLE_HEIGHT(VideotextConstants.VDT_SZDBLH),
    DOUBLE_SIZE(VideotextConstants.VDT_SZDBLHW)
}
