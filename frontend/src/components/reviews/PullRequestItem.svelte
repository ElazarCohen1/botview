<script lang="ts">
    import type {PullRequest} from "../../types/PullRequest";
    import type {Review} from "../../types/Review";
    import AnalysisDots from "../review/AnalysisDots.svelte";
    import ReviewStatusBadge from "../review/ReviewStatusBadge.svelte";

    let {
        pr,
        reviews = []
    }: {
        pr: PullRequest;
        reviews?: Review[];
    } = $props();


</script>
<div class="card p-3 mb-3 shadow-sm">
    <h5 class="card-title text-primary">
        #{pr.number} - {pr.name}
    </h5>
    <p class="card-text mb-2">
        Statut : <span class="badge bg-success">{pr.action}</span>
    </p>
    {#if reviews.length > 0}
        <ul class="list-group mb-3">
            {#each reviews as review (review.id)}
                <li class="list-group-item d-flex align-items-center gap-3">
                    <a href="#/reviews/{review.id}">Review #{review.id}</a>
                    <ReviewStatusBadge status={review.status}/>
                    <div class="ms-auto">
                        <AnalysisDots analyses={review.analyses}/>
                    </div>
                </li>
            {/each}
        </ul>
    {:else}
        <p class="small text-body-secondary">Aucune review pour cette PR.</p>
    {/if}
    <a
            class="btn btn-sm btn-outline-secondary align-self-start"
            href={pr.url}
            rel="noreferrer"
            target="_blank"
    >
        Voir sur GitHub ↗
    </a>
</div>
