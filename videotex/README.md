# Module videotex

Ce module contient des fonctions simples à utiliser pour facilement
créer une page Videotex en Kotlin.

## Utilisation

Le point d'entrée recommandé est la fonction `buildVideotex` :

```kotlin
import fr.outadoc.minipavi.videotex.buildVideotex

val videotex = buildVideotex {
    appendLine("Bonjour le monde !")
}
```

Vous pouvez appliquer des effets de style grâce aux fonctions `with*`

```kotlin
import fr.outadoc.minipavi.videotex.buildVideotex

val videotex = buildVideotex {
    withUnderline {
        appendLine("Bonjour le monde !")
    }
}
```

Ces fonctions sont imbriquables. Cet exemple produit un texte clignotant et souligné :

```kotlin
import fr.outadoc.minipavi.videotex.buildVideotex

val videotex = buildVideotex {
    withUnderline {
        withBlink {
            appendLine("Bonjour le monde !")
        }
    }
}
```
