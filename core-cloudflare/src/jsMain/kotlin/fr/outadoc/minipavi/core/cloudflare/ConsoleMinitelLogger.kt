package fr.outadoc.minipavi.core.cloudflare

import fr.outadoc.minipavi.core.MinitelLogger

/**
 * Un [MinitelLogger] qui écrit dans la console JS.
 *
 * Sur Cloudflare Workers, ces messages sont visibles avec `wrangler tail`.
 *
 * Les messages de niveau [MinitelLogger.Level.Debug] contiennent les requêtes complètes,
 * y compris l'adresse IP des utilisateurs : ils sont masqués par défaut.
 *
 * @param minLevel Le niveau minimal des messages à écrire.
 */
public class ConsoleMinitelLogger(
    private val minLevel: MinitelLogger.Level = MinitelLogger.Level.Warn,
) : MinitelLogger {
    /**
     * Écrit le message dans `console.debug`, `console.warn` ou `console.error` selon [level],
     * s'il est au moins de niveau [minLevel].
     */
    override fun log(
        level: MinitelLogger.Level,
        throwable: Throwable?,
        message: () -> String,
    ) {
        if (level < minLevel) return

        val text = message()
        when (level) {
            MinitelLogger.Level.Debug -> if (throwable == null) console.asDynamic().debug(text) else console.asDynamic().debug(text, throwable)
            MinitelLogger.Level.Warn -> if (throwable == null) console.warn(text) else console.warn(text, throwable)
            MinitelLogger.Level.Error -> if (throwable == null) console.error(text) else console.error(text, throwable)
        }
    }
}
