# Module core

Ce module contient tout ce qui est nécessaire pour construire un service
Minitel en Kotlin, indépendamment de la plateforme : JVM, JS ou Wasm.

## Utilisation

Le point d'entrée est la classe `MinitelServiceHandler`. Elle transforme le corps JSON
d'une requête de la passerelle en `GatewayRequest`, appelle votre code, et renvoie
le corps JSON de la réponse :

```kotlin
import fr.outadoc.minipavi.core.MinitelServiceHandler
import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minipavi.videotex.buildVideotex
import kotlinx.serialization.Serializable

@Serializable
object EtatSimple

val handler =
    MinitelServiceHandler<EtatSimple>(
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

fun main() {
    val responseBody: String = handler.handle(requestBody)
}
```

Ce module ne dépend d'aucune pile HTTP. Pour distribuer votre service, utilisez :

- `core-ktor` pour un serveur [Ktor](https://ktor.io) sur la JVM ;
- `core-cloudflare` pour [Cloudflare Workers](https://developers.cloudflare.com/workers/), avec Kotlin/JS.
