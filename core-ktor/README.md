# Module core-ktor

Ce module permet de distribuer un service Minitel construit avec le module `core`
à l'aide d'un serveur [Ktor](https://ktor.io).

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

Une requête dont le corps n'est pas une requête valide de la passerelle reçoit une réponse HTTP 400.
