package fr.outadoc.minipavi.sample.minitus

internal fun diff(
    expectedWord: String,
    guess: String,
): List<CharacterMatch> {
    val zipped = expectedWord.zip(guess)

    // On garde une copie modifiable des lettres disponibles
    val availableLetters: MutableList<Char> = expectedWord.toMutableList()

    // Les lettres avec un match exact ne peuvent pas être utilisées pour un match partiel,
    // donc on les retire de la liste des lettres disponibles.
    zipped.forEach { (expected, actual) ->
        if (expected == actual) {
            availableLetters.remove(actual)
        }
    }

    // On compare les lettres restantes pour trouver les matchs partiels
    return zipped.map { (expected, actual) ->
        when {
            expected == actual -> {
                CharacterMatch.Exact(actual)
            }

            availableLetters.contains(actual) -> {
                availableLetters.remove(actual)
                CharacterMatch.Partial(actual)
            }

            else -> {
                CharacterMatch.None(actual)
            }
        }
    }
}
