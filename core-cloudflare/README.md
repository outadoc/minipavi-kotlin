# Module core-cloudflare

Ce module permet de distribuer un service Minitel construit avec le module `core`
sur [Cloudflare Workers](https://developers.cloudflare.com/workers/), à l'aide de Kotlin/JS.

## Utilisation

Configurez la cible JS de votre projet pour produire des modules ES :

```kotlin
// build.gradle.kts
kotlin {
    js {
        nodejs()
        useEsModules()
        binaries.library()
    }

    sourceSets {
        jsMain.dependencies {
            implementation("com.github.outadoc.minipavi-kotlin:core-cloudflare:x.y.z")
        }
    }
}
```

Créez votre service avec `MinitelServiceHandler`, puis exportez une fonction `fetch` qui appelle
son gestionnaire. Exportez bien une fonction, et non une propriété : Kotlin/JS exporte les propriétés
sous forme d'accesseurs, que Cloudflare Workers ne sait pas appeler.

```kotlin
import fr.outadoc.minipavi.core.MinitelServiceHandler
import fr.outadoc.minipavi.core.cloudflare.Request
import fr.outadoc.minipavi.core.cloudflare.Response
import fr.outadoc.minipavi.core.cloudflare.toFetchHandler
import fr.outadoc.minipavi.core.model.ServiceResponse
import fr.outadoc.minipavi.videotex.buildVideotex
import kotlinx.serialization.Serializable
import kotlin.js.Promise

@Serializable
object EtatSimple

private val handler =
    MinitelServiceHandler<EtatSimple>(
        version = "0.1",
        initialState = { EtatSimple },
    ) { request ->
        ServiceResponse(
            state = EtatSimple,
            content = buildVideotex { appendLine("Bonjour le monde !") },
        )
    }.toFetchHandler(path = "/")

@OptIn(ExperimentalJsExport::class)
@JsExport
fun fetch(request: Request): Promise<Response> = handler(request)
```

Cloudflare Workers attend un export par défaut, que Kotlin/JS ne sait pas produire.
Ajoutez donc un point d'entrée JS de deux lignes, référencé par `main` dans votre `wrangler.toml` :

```js
// src/index.mjs
import { fetch } from "../build/dist/js/productionLibrary/mon-service.mjs";
export default { fetch };
```

Si votre service utilise `kotlinx-datetime`, ajoutez `@js-joda/core` aux dépendances npm de votre Worker.

Le gestionnaire répond 404 si le chemin ne correspond pas, 405 pour une méthode autre que `POST`,
et 400 si le corps n'est pas une requête valide de la passerelle.

Voir [`sample/cloudflare-worker`](../sample/cloudflare-worker) pour un exemple complet.
