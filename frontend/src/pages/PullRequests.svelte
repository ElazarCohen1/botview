<script lang="ts">
    import {onMount} from "svelte";
    import {getPullRequest} from "../api/pullRequest/pullRequest";
    import PullRequestItem from "../components/reviews/PullRequestItem.svelte";
    import type {PullRequest} from "../types/PullRequest";
    import {startPolling} from "../lib/polling";
    import {gitProvider} from "../lib/stores.svelte";
    import type {Review} from "../types/Review";
    import {getReviews} from "../api/review/reviews";


    let pullRequests: PullRequest[] = $state([]);
    let repoName: string = $state("");
    let loading: boolean = $state(true);
    let error: string = $state("");

    let reviews: Review[] = $state([]);
    let reviewError: string = $state("");

    $effect(() => {
        const provider = gitProvider.selected ?? undefined;

        return startPolling(
            () => getReviews(provider),
            (data) => {
                reviews = data as Review[];
                reviewError = "";
                return true;
            },
            (message) => reviewError = message
        );
    });

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

        // pr=12 dans l'URL : on n'affiche que cette PR
        const prNumber = Number(params.get("pr"));
        const all = await getPullRequest(repoName, gitProvider.selected ?? undefined);
        const data = prNumber ? all.filter(p => p.number === prNumber) : all;
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
        <a class="btn btn-sm btn-outline-secondary" href="#/repositories">
            ← Retour aux dépôts
        </a>
    </div>

    {#if reviewError}
        <div class="alert alert-warning">{reviewError}</div>
    {/if}

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
            <PullRequestItem
                    {pr}
                    reviews={reviews.filter(r => r.repositoryName === repoName && r.pullRequestNumber === pr.number)}
            />
        {/each}
    {/if}
</div>
