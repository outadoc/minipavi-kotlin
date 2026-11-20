# MiniPavi-Kotlin

SDK pour le développement de services [Minitel][1] sur la plateforme [MiniPavi][2] en
Kotlin.

## Pourquoi ?

MiniPavi propose une API ainsi qu'un SDK pour le développement de services Minitel en PHP.

PHP, c'est sympa, mais mon truc, c'est Kotlin. Pourquoi ne donc pas proposer un SDK pour Kotlin se reposant sur l'API de
MiniPavi, et permettre à mes ami·e·s développeur·euse·s Kotlin de créer des services Minitel ?

Vous trouverez donc ici le résultat de ces efforts : un SDK Kotlin pour MiniPavi, vous permettant **enfin** d'allier vos
compétences en Kotlin et votre amour pour le GiscardPunk.

### Non mais, pourquoi ?

Pourquoi pas ?

## Services d'exemple

Vous pouvez consulter le contenu du module [`sample`](sample) pour voir comment utiliser le SDK à l'aide d'exemples
concrets.

## Services en ligne

- [`5422*MINITUS`](https://github.com/outadoc/minitus) : Motus-like sur Minitel, avec un nouveau mot à trouver chaque jour.

## Utilisation du SDK

Tout d'abord, je vous invite à lire la documentation de MiniPavi, disponible [sur son site][2] ainsi que [sur son dépôt
GitHub][3]. Vous y trouverez des informations essentielles sur la topologie de MiniPavi. En lisant cette documentation,
gardez en tête que vous pouvez substituer les références au développement PHP, que vous pourrez effectuer en Kotlin à la
place.

MiniPavi-Kotlin est un SDK Kotlin Multiplatform. MiniPavi va appeler votre service, qui construira
sa réponse avec le SDK, et la renverra à MiniPavi. Le SDK est découpé en modules :

| Module            | Cibles          | Rôle                                                           |
|-------------------|-----------------|----------------------------------------------------------------|
| `core`            | JVM, JS, Wasm   | Modèles et `MinitelServiceHandler`, sans dépendance HTTP.      |
| `videotex`        | JVM, JS, Wasm   | Construction de pages Vidéotex.                                |
| `core-ktor`       | JVM             | Distribution du service par un serveur [Ktor][4].              |
| `core-cloudflare` | JS              | Distribution du service par [Cloudflare Workers][6].           |

Les exemples ci-dessous utilisent Ktor. Pour Cloudflare Workers, consultez le
[README de `core-cloudflare`](core-cloudflare/README.md) et l'exemple
[`sample/cloudflare-worker`](sample/cloudflare-worker).

Ajoutez la dépendance à MiniPavi-Kotlin dans votre projet :

```toml
# libs.versions.toml

[versions]
minipavi-kotlin = "x.y.z"

[libraries]
minipavi-core-ktor = { module = "com.github.outadoc.minipavi-kotlin:core-ktor", version.ref = "minipavi-kotlin" }
minipavi-videotex = { module = "com.github.outadoc.minipavi-kotlin:videotex", version.ref = "minipavi-kotlin" }
```

```kotlin
// settings.gradle.kts

dependencyResolutionManagement {
    repositories {
        maven("https://jitpack.io")
    }
}
```

```kotlin
// build.gradle.kts

dependencies {
    implementation(libs.minipavi.core.ktor)
    implementation(libs.minipavi.videotex)
}
```

> [!NOTE]
> Depuis la version 0.1.0, l'intégration Ktor (`minitelService`) est dans le module `core-ktor`.
> Si vous utilisiez `core` avec Ktor, remplacez simplement cette dépendance par `core-ktor` :
> le paquet et les signatures sont inchangés.

Commencez par créer un serveur Ktor de base. Je vous recommande encore une fois de suivre la [documentation de Ktor][4]
si vous n'êtes pas familièr·e avec ce framework.

```kotlin
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module,
    ).start(wait = true)
}

fun Application.module() {
    TODO("Votre service sera ici")
}
```

Ensuite, vous aurez besoin de définir une structure qui représentera l'état d'une session utilisateur.
Cet état pourra être persisté au cours de la session, et peut être aussi simple ou aussi complexe que vous le souhaitez,
du moment qu'il peut être serialisé et désérialisé par [`kotlinx.serialization`][5].

```kotlin
import kotlinx.serialization.Serializable

// Un état tout simple, constant, pour une application stateless
@Serializable
object EtatSimple

// Un état un peu plus complexe, avec un compteur
@Serializable
data class EtatCompteur(val nbVisites: Int)

// Une structure avec plusieurs états définis
@Serializable
sealed class Etat {
    @Serializable
    @SerialName("formulaire")
    data object Formulaire : Etat()

    @Serializable
    @SerialName("resultat")
    data class AffichageResultat(
        val nom: String,
        val prenom: String,
    ) : Etat()
}
```

Une fois que vous avez votre état, vous pouvez commencer à définir votre service.

```kotlin
import fr.outadoc.minipavi.core.ktor.minitelService
import fr.outadoc.minipavi.core.model.ServiceResponse
import io.ktor.server.application.Application

fun Application.module() {
    // Le module ktor de tout à l'heure. On lui ajoute notre service.
    helloWorld()
}

fun Application.helloWorld() {
    minitelService<EtatSimple>(
        path = "/",
        version = "0.1",
        initialState = { EtatSimple },
    ) { request ->
        TODO("Votre logique de service sera ici")
    }
}
```

Le paramètre `request` de la lambda contient le contenu de la requête envoyée par la passerelle MiniPavi.
Vous pouvez également accéder aux autres informations de la requête comme n'importe quelle définition de `Route` de
Ktor.

Il ne vous reste plus qu'à construire votre réponse, en renvoyant une `ServiceResponse` :

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

La fonction `buildVideotex` vous permet de construire une réponse au format Vidéotex.
Vous pouvez y ajouter du texte, des couleurs, des caractères spéciaux, etc.
Elle est disponible dans le module `videotex`.

Et voilà. Plus qu'à lancer votre serveur, et à le connecter à MiniPavi. 🎉

[1]: https://fr.wikipedia.org/wiki/Minitel

[2]: https://www.minipavi.fr/

[3]: https://github.com/ludosevilla/minipavi

[4]: https://ktor.io/docs/server-create-a-new-project.html

[5]: https://github.com/Kotlin/kotlinx.serialization

[6]: https://developers.cloudflare.com/workers/
