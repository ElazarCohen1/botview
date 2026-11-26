<script lang="ts">
    import {getRepositories} from "../api/repositories/repositories";
    import {getNbPullRequest} from "../api/pullRequest/pullRequest";
    import {getReviews} from "../api/review/reviews";
    import {startPolling} from "../lib/polling";
    import {isFinished} from "../lib/reviewStatus";
    import {gitProvider} from "../lib/stores.svelte";
    import type {Repository} from "../types/Repository";
    import type {Review} from "../types/Review";
    import DashboardHeader from "../components/layout/DashboardHeader.svelte";
    import Section from "../components/common/Section.svelte";
    import StatCard from "../components/common/StatCard.svelte";
    import EmptyState from "../components/common/EmptyState.svelte";
    import RepoCard from "../components/repos/RepoCard.svelte";
    import ReviewCard from "../components/review/ReviewCard.svelte";
    import ReviewQueue from "../components/review/ReviewQueue.svelte";

    let connectedRepos: Repository[] = $state([]);
    let reviews: Review[] = $state([]);
    let prAnalysed: number = $state(0);
    let loading: boolean = $state(true);
    let loadError: string = $state("");
    let reviewError: string = $state("");

    let sorted = $derived([...reviews].sort((a, b) => b.createdAt.localeCompare(a.createdAt)));
    let queue = $derived(sorted.filter(r => !isFinished(r.status)));
    let latest = $derived(sorted.filter(r => isFinished(r.status)).slice(0, 5));
    let blocking = $derived(reviews.filter(r => r.isBlocking).length);

    // Dépôts et nombre de PR : rechargés seulement si on change de provider
    $effect(() => {
        const provider = gitProvider.selected ?? undefined;
        let cancelled = false;
        loadError = "";

        Promise.all([getRepositories(provider), getNbPullRequest()])
            .then(([repos, count]) => {
                if (cancelled) return;
                connectedRepos = repos.filter(r => r.has_webhook);
                prAnalysed = count;
            })
            .catch((e) => {
                if (!cancelled) loadError = e instanceof Error ? e.message : "Erreur inattendue.";
            });

        return () => {
            cancelled = true;
        };
    });

    // Reviews : rafraîchies en continu
    $effect(() => {
        const provider = gitProvider.selected ?? undefined;
        reviews = [];
        loading = true;

        return startPolling(
            () => getReviews(provider),
            (data) => {
                reviews = data;
                reviewError = "";
                loading = false;
                return true;
            },
            (message) => {
                reviewError = message;
                loading = false;
            }
        );
    });
</script>

<DashboardHeader/>

{#if loadError}
    <div class="alert alert-danger">{loadError}</div>
{/if}
{#if reviewError}
    <div class="alert alert-danger">{reviewError}</div>
{/if}

<div class="row g-3 mb-4">
    <div class="col-6 col-lg-3">
        <StatCard icon="bi-git" label="Repos connectés" value={connectedRepos.length}/>
    </div>
    <div class="col-6 col-lg-3">
        <StatCard color="info" icon="bi-git" label="PR analysées" value={prAnalysed}/>
    </div>
    <div class="col-6 col-lg-3">
        <StatCard color="warning" icon="bi-hourglass-split" label="Reviews en file" value={queue.length}/>
    </div>
    <div class="col-6 col-lg-3">
        <StatCard color="danger" icon="bi-exclamation-octagon" label="Reviews bloquantes" value={blocking}/>
    </div>
</div>

{#if queue.length > 0}
    <Section icon="bi-hourglass-split" title="File d'attente">
        <ReviewQueue reviews={queue}/>
    </Section>
{/if}

<Section icon="bi-chat-square-text" title="Dernières reviews">
    {#if loading}
        <p>Chargement des reviews...</p>
    {:else}
        {#each latest as review (review.id)}
            <ReviewCard {review}/>
        {:else}
            <EmptyState message="Aucune review terminée"/>
        {/each}
        <a href="#/reviews">Voir toutes les reviews →</a>
    {/if}
</Section>

<Section icon="bi-folder2-open" title="Repos connectés">
    {#if connectedRepos.length === 0}
        <EmptyState message="Aucun repo connecté"/>
    {:else}
        <div class="row g-3">
            {#each connectedRepos as repo (repo.id)}
                <div class="col-md-6 col-lg-4">
                    <RepoCard {repo} reviews={sorted.filter(r => r.repositoryName === repo.name)}/>
                </div>
            {/each}
        </div>
    {/if}
</Section>
