<script lang="ts">
    import type {Review} from "../../types/Review";
    import AnalysisDots from "./AnalysisDots.svelte";
    import ReviewStatusBadge from "./ReviewStatusBadge.svelte";

    let {review}: { review: Review } = $props();
</script>

<div class="card p-3 mb-3 shadow-sm">
    <div class="d-flex justify-content-between align-items-start gap-3">
        <h5 class="card-title mb-1">
            <a class="text-decoration-none" href="#/reviews/{review.id}">
                Review #{review.id} : {review.pullRequestTitle}
            </a>
        </h5>
        <AnalysisDots analyses={review.analyses}/>
    </div>

    <p class="text-body-secondary small mb-2">
        {review.repositoryName} ·
        <a href="#/pull-requests?repo={encodeURIComponent(review.repositoryName)}&pr={review.pullRequestNumber}">
            PR #{review.pullRequestNumber}
        </a>
        · {new Date(review.createdAt).toLocaleString("fr-FR")}
    </p>

    <div class="d-flex gap-2">
        <ReviewStatusBadge status={review.status}/>
        {#if review.isBlocking}
            <span class="badge text-bg-danger">Bloquante</span>
        {/if}
    </div>
</div>
