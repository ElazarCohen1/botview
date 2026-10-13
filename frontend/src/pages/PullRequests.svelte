<script>
    import { onMount } from "svelte";
    import { getPullRequest } from "../api/pullRequest/pullRequest";
    import PullRequestItem from "../components/reviews/PullRequestItem.svelte";
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

            const data = await getPullRequest(repoName) ;
            if (data) {
                pullRequests = data;
            } else {
                error = "Impossible de récupérer les Pull Requests.";
            }
        loading = false;
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
            <PullRequestItem {pr}/>
        {/each}
    {/if}
</div>
