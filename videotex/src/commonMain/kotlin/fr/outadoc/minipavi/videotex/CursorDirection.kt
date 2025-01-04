package fr.outadoc.minipavi.videotex

/**
 * Direction du curseur pour les déplacements.
 */
public enum class CursorDirection(
    internal val code: Byte
) {
    /**
     * Déplacement vers la gauche.
     */
    Left(VdtConstants.VDT_LEFT),

    /**
     * Déplacement vers la droite.
     */
    Right(VdtConstants.VDT_RIGHT),

    /**
     * Déplacement vers le bas.
     */
    Down(VdtConstants.VDT_DOWN),

    /**
     * Déplacement vers le haut.
     */
    Up(VdtConstants.VDT_UP),

    /**
     * Déplacement au début de la ligne.
     */
    LineStart(VdtConstants.VDT_CR),
}
