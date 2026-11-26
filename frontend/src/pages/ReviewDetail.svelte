<script lang="ts">
    import {getReview} from "../api/review/reviews";
    import {startPolling} from "../lib/polling";
    import {isFinished} from "../lib/reviewStatus";
    import type {Review} from "../types/Review";
    import AnalysisDots from "../components/review/AnalysisDots.svelte";
    import AnalysisList from "../components/review/AnalysisList.svelte";
    import ReviewStatusBadge from "../components/review/ReviewStatusBadge.svelte";
    import ReviewFiles from "../components/review/ReviewFiles.svelte";

    let {params}: { params?: { id: string } } = $props();

    let review: Review | null = $state(null);
    let error: string = $state("");

    $effect(() => {
        const id = Number(params?.id);
        review = null;
        error = "";

        if (!Number.isInteger(id) || id <= 0) {
            error = "Identifiant de review invalide.";
            return;
        }

        return startPolling(
            () => getReview(id),
            (data) => {
                review = data;
                error = "";
                return !isFinished(data.status); // plus de requêtes une fois finie
            },
            (message) => error = message
        );
    });
</script>

<a class="btn btn-sm btn-outline-secondary mb-3" href="#/reviews">← Toutes les reviews</a>

{#if error}
    <div class="alert alert-danger">{error}</div>
{/if}

{#if review}
    <div class="d-flex justify-content-between align-items-start gap-3 mb-2">
        <h2>Review #{review.id} : {review.pullRequestTitle}</h2>
        <AnalysisDots analyses={review.analyses}/>
    </div>

    <p class="text-body-secondary">
        {review.repositoryName} ·
        <a href="#/pull-requests?repo={encodeURIComponent(review.repositoryName)}&pr={review.pullRequestNumber}">
            PR #{review.pullRequestNumber}
        </a>
        · {new Date(review.createdAt).toLocaleString("fr-FR")}
    </p>

    <div class="d-flex gap-2 mb-4">
        <ReviewStatusBadge status={review.status}/>
        {#if review.isBlocking}
            <span class="badge text-bg-danger">Bloquante</span>
        {/if}
    </div>

    <h4>Commentaire global</h4>
    <div class="card p-3 mb-4">
        {review.globalComment ?? "Pas encore de commentaire."}
    </div>

    <h4>Analyses statiques</h4>
    <AnalysisList analyses={review.analyses}/>

    <h4 class="mt-4">Fichiers modifiés</h4>
    {#key review.id}
        <ReviewFiles finished={isFinished(review.status)} reviewId={review.id}/>
    {/key}
{:else if !error}
    <p>Chargement de la review...</p>
{/if}
