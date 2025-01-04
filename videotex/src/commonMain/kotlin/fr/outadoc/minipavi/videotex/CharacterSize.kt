package fr.outadoc.minipavi.videotex

import kotlinx.io.bytestring.ByteString

/**
 * Taille de caractère.
 */
public enum class CharacterSize(
    internal val code: ByteString
) {
    /**
     * Caractères en taille standard.
     */
    Normal(VdtConstants.VDT_SZNORM),

    /**
     * Caractères en double largeur.
     */
    DoubleWidth(VdtConstants.VDT_SZDBLW),

    /**
     * Caractères en double hauteur.
     */
    DoubleHeight(VdtConstants.VDT_SZDBLH),

    /**
     * Caractères en double largeur et double hauteur.
     */
    DoubleSize(VdtConstants.VDT_SZDBLHW)
}
