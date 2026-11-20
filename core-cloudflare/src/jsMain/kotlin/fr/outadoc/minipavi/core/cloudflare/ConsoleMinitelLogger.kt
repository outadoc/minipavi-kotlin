package fr.outadoc.minipavi.core.cloudflare

import fr.outadoc.minipavi.core.MinitelLogger

/**
 * Un [MinitelLogger] qui écrit dans la console JS.
 *
 * Sur Cloudflare Workers, ces messages sont visibles avec `wrangler tail`.
 */
public object ConsoleMinitelLogger : MinitelLogger {
    /**
     * Écrit le message dans `console.log`, `console.warn` ou `console.error` selon [level].
     */
    override fun log(
        level: MinitelLogger.Level,
        throwable: Throwable?,
        message: () -> String,
    ) {
        when (level) {
            MinitelLogger.Level.Debug -> console.log(message(), throwable)
            MinitelLogger.Level.Warn -> console.warn(message(), throwable)
            MinitelLogger.Level.Error -> console.error(message(), throwable)
        }
    }
}
