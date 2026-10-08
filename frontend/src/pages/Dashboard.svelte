<script>
    import { user } from '../lib/stores';
    import { connectedRepos, reviews } from '../lib/mock.js';
    import DashboardHeader from '../components/layout/DashboardHeader.svelte';
    import Section from '../components/common/Section.svelte';
    import StatCard from '../components/common/StatCard.svelte';
    import EmptyState from '../components/common/EmptyState.svelte';
    import RepoCard from '../components/repos/RepoCard.svelte';
    import ReviewTable from '../components/reviews/ReviewTable.svelte';

    $: blocking = reviews.filter((r) => r.blocking).length;
</script>

<DashboardHeader />

{#if !$user}
    <EmptyState icon="bi-plug" message="Connecte-toi à un provider Git pour commencer." />
{:else}
    <div class="row g-3 mb-4">
        <div class="col-6 col-lg-3"><StatCard label="Repos connectés" value={connectedRepos.length} icon="bi-git" /></div>
        <div class="col-6 col-lg-3"><StatCard label="PR analysées" value={reviews.length} icon="bi-git" color="info" /></div>
        <div class="col-6 col-lg-3"><StatCard label="Reviews bloquantes" value={blocking} icon="bi-exclamation-octagon" color="danger" /></div>
        <div class="col-6 col-lg-3"><StatCard label="Dernière review" value={reviews[0].date} icon="bi-clock-history" color="success" /></div>
    </div>

    <Section title="Repos connectés" icon="bi-folder2-open">
        {#if connectedRepos.length === 0}
            <EmptyState message="Aucun repo connecté" />
        {:else}
            <div class="row g-3">
                {#each connectedRepos as repo (repo.id)}
                    <div class="col-md-6 col-lg-4"><RepoCard {repo} /></div>
                {/each}
            </div>
        {/if}
    </Section>

    <Section title="Dernières reviews" icon="bi-chat-square-text">
        <ReviewTable {reviews} />
    </Section>
{/if}