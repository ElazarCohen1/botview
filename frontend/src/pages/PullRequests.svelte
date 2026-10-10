<script>
    import { onMount } from "svelte";

    /** @type {any[]} */
    let pullRequests = $state([]);
    let repoName = $state("");
    let loading = $state(true);
    let error = $state("");

    onMount(async () => {
        const hash = window.location.hash;
        const params = new URLSearchParams(
            hash.includes("?") ? hash.split("?")[1] : "",
        );
        repoName = params.get("repo") || "";

        if (!repoName) {
            error = "Aucun dépôt spécifié.";
            loading = false;
            return;
        }

        try {
            const res = await fetch(`/api/pullRequest/${repoName}`);
            if (res.ok) {
                pullRequests = await res.json();
            } else {
                error = "Impossible de récupérer les Pull Requests.";
            }
        } catch (e) {
            console.error("Erreur réseau", e);
            error = "Impossible de contacter le serveur.";
        } finally {
            loading = false;
        }
    });
</script>

<div>
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Pull Requests : {repoName}</h2>
        <a href="#/repositories" class="btn btn-sm btn-outline-secondary">
            ← Retour aux dépôts
        </a>
    </div>

    {#if loading}
        <p>Chargement des Pull Requests...</p>
    {:else if error}
        <div class="alert alert-danger">
            {error}
        </div>
    {:else if pullRequests.length === 0}
        <div class="alert alert-secondary">
            Aucune Pull Request pour ce dépôt.
        </div>
    {:else}
        {#each pullRequests as pr}
            <div class="card p-3 mb-3 shadow-sm">
                <h5 class="card-title text-primary">
                    #{pr.number} - {pr.title}
                </h5>
                <p class="card-text mb-2">
                    Statut : <span class="badge bg-success">{pr.state}</span>
                </p>
                <a
                    href={pr.html_url}
                    target="_blank"
                    rel="noreferrer"
                    class="btn btn-sm btn-outline-secondary"
                >
                    Voir sur GitHub ↗
                </a>
            </div>
        {/each}
    {/if}
</div>
