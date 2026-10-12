<script>
    import {onMount} from "svelte";

    /** @type {any[]} */
    let repos = $state([]);
    let loading = $state(true);
    let error = $state("");

    onMount(async () => {
        try {
            const res = await fetch("/api/repositories");
            if (res.ok) {
                repos = await res.json();
            } else {
                error = "Impossible de récupérer les dépôts.";
            }
        } catch (e) {
            console.error("Erreur réseau", e);
            error = "Impossible de contacter le serveur.";
        } finally {
            loading = false;
        }
    });

    async function name() {
        loading = true;
        error = "";
        try {
            const res = await fetch("/api/repositories");
            if (res.ok) {
                repos = await res.json();
            } else {
                error = "Impossible de relancer la page ";
            }
        } catch (e) {
            console.error("Erreur réseau", e);
            error = "Impossible de contacter le serveur.";
        } finally {
            loading = false;
        }
    }
</script>

<div>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Dépôts</h2>
        <button onclick={name} class="btn btn-outline-primary btn-sm">
            Rafraîchir
        </button>
    </div>

    {#if loading}
        <p>Chargement des dépôts...</p>
    {:else if error}
        <div class="alert alert-danger">
            {error}
        </div>
    {:else if repos.length === 0}
        <div class="alert alert-secondary">Aucun dépôt trouvé.</div>
    {:else}
        {#each repos as repo}
            <div class="card p-3 mb-3 shadow-sm">
                <h5 class="card-title text-primary">{repo.name}</h5>
                <p class="card-text text-muted mb-2">
                    Branche : <code>{repo.default_branch}</code>
                </p>
                <div>
                    <a
                        href="#/pull-requests?repo={repo.name}"
                        class="btn btn-sm btn-primary me-2"
                    >
                        Voir les Pull Requests
                    </a>
                    <a
                        href={repo.html_url}
                        target="_blank"
                        rel="noreferrer"
                        class="btn btn-sm btn-outline-secondary"
                    >
                        GitHub ↗
                    </a>
                </div>
            </div>
        {/each}
    {/if}
</div>