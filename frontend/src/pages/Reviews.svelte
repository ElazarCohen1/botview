<script lang="ts">
    import {getReviews} from "../api/review/reviews";
    import {startPolling} from "../lib/polling";
    import {isFinished} from "../lib/reviewStatus";
    import {gitProvider} from "../lib/stores.svelte";
    import type {Review} from "../types/Review";
    import ReviewCard from "../components/review/ReviewCard.svelte";
    import ReviewQueue from "../components/review/ReviewQueue.svelte";
    import EmptyState from "../components/common/EmptyState.svelte";

    let reviews: Review[] = $state([]);
    let loading: boolean = $state(true);
    let error: string = $state("");

    let queue = $derived(reviews.filter(r => !isFinished(r.status)));
    let finished = $derived(
        reviews
            .filter(r => isFinished(r.status))
            .sort((a, b) => b.createdAt.localeCompare(a.createdAt))
    );

    // Relancé si on change de provider, arrêté quand on quitte la page
    $effect(() => {
        const provider = gitProvider.selected ?? undefined;
        reviews = [];
        loading = true;

        return startPolling(
            () => getReviews(provider),
            (data) => {
                reviews = data;
                error = "";
                loading = false;
                return true; // on continue : de nouvelles reviews peuvent arriver
            },
            (message) => {
                error = message;
                loading = false;
            }
        );
    });
</script>

<h2 class="mb-4">Reviews</h2>

{#if error}
    <div class="alert alert-danger">{error}</div>
{/if}

{#if loading}
    <p>Chargement des reviews...</p>
{:else}
    <section class="mb-5">
        <h4 class="mb-3">File d'attente ({queue.length})</h4>
        {#if queue.length === 0}
            <EmptyState message="Aucune review en attente"/>
        {:else}
            <ReviewQueue reviews={queue}/>
        {/if}
    </section>

    <section>
        <h4 class="mb-3">Dernières reviews</h4>
        {#each finished as review (review.id)}
            <ReviewCard {review}/>
        {:else}
            <EmptyState message="Aucune review terminée"/>
        {/each}
    </section>
{/if}
