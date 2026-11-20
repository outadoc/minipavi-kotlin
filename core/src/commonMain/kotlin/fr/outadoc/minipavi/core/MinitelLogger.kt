package fr.outadoc.minipavi.core

/**
 * Reçoit les messages de diagnostic émis par un [MinitelServiceHandler].
 *
 * Chaque module d'intégration fournit sa propre implémentation, branchée sur
 * le système de logs de la plateforme.
 */
public fun interface MinitelLogger {

    /**
     * Émet un message de log.
     *
     * @param level La sévérité du message.
     * @param throwable L'exception associée au message, le cas échéant.
     * @param message Le message, évalué uniquement si nécessaire.
     */
    public fun log(
        level: Level,
        throwable: Throwable?,
        message: () -> String,
    )

    /**
     * Émet un message de log sans exception associée.
     *
     * @param level La sévérité du message.
     * @param message Le message, évalué uniquement si nécessaire.
     */
    public fun log(
        level: Level,
        message: () -> String,
    ): Unit = log(level, null, message)

    /**
     * Sévérité d'un message de log.
     */
    public enum class Level {
        /** Message de débogage. */
        Debug,

        /** Avertissement. */
        Warn,

        /** Erreur. */
        Error,
    }

    /**
     * Implémentations courantes de [MinitelLogger].
     */
    public companion object {
        /**
         * Un [MinitelLogger] qui ignore tous les messages.
         */
        public val None: MinitelLogger = MinitelLogger { _, _, _ -> }
    }
}
