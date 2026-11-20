# Exemple : Cloudflare Workers

Un service Minitel distribué par [Cloudflare Workers](https://developers.cloudflare.com/workers/),
à l'aide de Kotlin/JS et du module `core-cloudflare`.

```shell
npm ci
npm run dev     # compile le service et lance un Worker local sur http://localhost:8787
npm run deploy  # compile le service et le déploie sur votre compte Cloudflare
```

Le fichier [`worker/index.mjs`](worker/index.mjs) réexporte le gestionnaire `fetch` produit par Kotlin/JS
en tant qu'export par défaut, comme l'attend Cloudflare Workers.
