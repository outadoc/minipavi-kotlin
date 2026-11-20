// Cloudflare Workers attend un export par défaut, que Kotlin/JS ne sait pas produire.
import { fetch } from "../build/dist/js/productionLibrary/cloudflare-worker.mjs";

export default { fetch };
