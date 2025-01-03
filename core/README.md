# Module core

Ce module contient tout ce qui est nécessaire pour construire un service
Minitel en Kotlin, distribué par un serveur Ktor.

## Utilisation

Le point d'entrée recommandé est la fonction `minitelService`, que vous devez utiliser pour
configurer votre serveur Ktor :

```kotlin
import fr.outadoc.minipavi.core.ktor.minitelService
import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minipavi.videotex.buildVideotex
import io.ktor.server.application.Application
import kotlinx.serialization.Serializable

@Serializable
object EtatSimple

fun Application.helloWorld() {
    minitelService<EtatSimple>(
        path = "/",
        version = "0.1",
        initialState = { EtatSimple },
    ) { request ->
        ServiceResponse(
            state = EtatSimple,
            content =
                buildVideotex {
                    appendLine("Bonjour le monde !")
                },
        )
    }
}
```
