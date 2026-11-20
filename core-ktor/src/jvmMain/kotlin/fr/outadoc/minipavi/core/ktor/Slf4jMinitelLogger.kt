package fr.outadoc.minipavi.core.ktor

import fr.outadoc.minipavi.core.MinitelLogger
import org.slf4j.Logger

/**
 * Transmet les messages d'un [fr.outadoc.minipavi.core.MinitelServiceHandler] au [Logger] de Ktor.
 */
internal class Slf4jMinitelLogger(
    private val logger: Logger,
) : MinitelLogger {
    override fun log(
        level: MinitelLogger.Level,
        throwable: Throwable?,
        message: () -> String,
    ) {
        when (level) {
            MinitelLogger.Level.Debug -> if (logger.isDebugEnabled) logger.debug(message(), throwable)
            MinitelLogger.Level.Warn -> if (logger.isWarnEnabled) logger.warn(message(), throwable)
            MinitelLogger.Level.Error -> if (logger.isErrorEnabled) logger.error(message(), throwable)
        }
    }
}
