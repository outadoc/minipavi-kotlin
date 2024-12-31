package fr.outadoc.minipavi.sample.minitus.dictionary

import fr.outadoc.minipavi.sample.minitus.GameConstants
import fr.outadoc.minipavi.sample.minitus.normalize
import fr.outadoc.minipavi.sample.minitus.readResource
import io.ktor.server.application.ApplicationEnvironment
import kotlinx.io.readLineStrict

private const val MAX_READ_LINE_LENGTH = 32L
private const val DICT_PATH = "/dict/fr/dict.txt"

internal fun readWords(environment: ApplicationEnvironment): Set<String> =
    buildSet {
        readResource(DICT_PATH)
            .use { wordSource ->
                try {
                    while (!wordSource.exhausted()) {
                        val word =
                            wordSource
                                .readLineStrict(
                                    limit = MAX_READ_LINE_LENGTH,
                                )
                                .normalize()

                        if (word.matches(GameConstants.ALLOWED_WORD_REGEX)) {
                            add(word)
                            environment.log.debug("AJOUTÉ : $word")
                        } else {
                            environment.log.debug("IGNORÉ : $word")
                        }
                    }
                } catch (e: Exception) {
                    environment.log.error("Erreur lors de la lecture du dictionnaire", e)
                }
            }

        environment.log.info("Chargé $size mots avec succès")
    }
