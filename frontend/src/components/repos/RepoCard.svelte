<script lang="ts">
    import type {Repository} from "../../types/Repository";
    import type {Review} from "../../types/Review";
    import ReviewStatusBadge from "../review/ReviewStatusBadge.svelte";

    // `reviews` : celles de ce dépôt, la plus récente en premier
    let {repo, reviews = []}: { repo: Repository; reviews?: Review[] } = $props();
</script>

<div class="card h-100">
    <div class="card-body">
        <h6 class="card-title text-break">{repo.name}</h6>
        <p class="text-body-secondary small mb-2">{reviews.length} review(s)</p>

        {#if reviews[0]}
            <a class="d-flex align-items-center gap-2 mb-3 small" href="#/reviews/{reviews[0].id}">
                Dernière : PR #{reviews[0].pullRequestNumber}
                <ReviewStatusBadge status={reviews[0].status}/>
            </a>
        {/if}

        <a class="btn btn-sm btn-outline-primary" href="#/pull-requests?repo={encodeURIComponent(repo.name)}">
            Voir les Pull Requests
        </a>
    </div>
</div>
