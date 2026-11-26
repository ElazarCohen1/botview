<script lang="ts">
    import type {Review} from "../../types/Review";
    import AnalysisDots from "./AnalysisDots.svelte";
    import ReviewStatusBadge from "./ReviewStatusBadge.svelte";
    import {isFinished} from "../../lib/reviewStatus";

    let {reviews}: { reviews: Review[] } = $props();

    // La plus ancienne à gauche : c'est la première de la file
    let sorted = $derived(
        [...reviews].sort((a, b) => a.createdAt.localeCompare(b.createdAt))
    );

    const progress = (review: Review): number =>
        review.analyses.length === 0
            ? 0
            : 100 * review.analyses.filter(a => isFinished(a.status)).length / review.analyses.length;
</script>

<div class="d-flex gap-3 overflow-auto pb-2">
    {#each sorted as review (review.id)}
        <a class="card queue-card p-3 text-decoration-none flex-shrink-0" href="#/reviews/{review.id}">
            <div class="d-flex justify-content-between align-items-center mb-2">
                <ReviewStatusBadge status={review.status}/>
                <AnalysisDots analyses={review.analyses}/>
            </div>
            <div class="fw-semibold text-truncate">{review.pullRequestTitle}</div>
            <div class="small text-body-secondary mb-2">
                {review.repositoryName} · PR #{review.pullRequestNumber}
            </div>
            <div aria-label="Progression" aria-valuemax="100" aria-valuemin="0"
                 aria-valuenow={progress(review)} class="progress" role="progressbar" style="height: 4px">
                <div class="progress-bar" style="width: {progress(review)}%"></div>
            </div>
        </a>
    {/each}
</div>

<style>
    .queue-card {
        width: 17rem;
    }
</style>
