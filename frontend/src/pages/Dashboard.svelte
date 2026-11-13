<script lang="ts">
    import {reviews} from '../lib/mock.js';
    import {onMount} from 'svelte';
    import {getRepositories} from '../api/repositories/repositories';
    import DashboardHeader from '../components/layout/DashboardHeader.svelte';
    import Section from '../components/common/Section.svelte';
    import StatCard from '../components/common/StatCard.svelte';
    import EmptyState from '../components/common/EmptyState.svelte';
    import RepoCard from '../components/repos/RepoCard.svelte';
    import ReviewTable from '../components/reviews/ReviewTable.svelte';
    import type {Repository} from "../types/Repository";

    let connectedRepos: Repository[] = $state([]);

    onMount(async () => {
        const data: Repository[] = (await getRepositories()).filter(r => r.has_webhook == true);
        connectedRepos = data ?? [];
    });
    let blocking = $derived(reviews.filter((r) => r.blocking).length);


</script>

<DashboardHeader/>

<div class="row g-3 mb-4">
    <div class="col-6 col-lg-3">
        <StatCard icon="bi-git" label="Repos connectés" value={connectedRepos.length}/>
    </div>
    <div class="col-6 col-lg-3">
        <StatCard color="info" icon="bi-git" label="PR analysées" value={reviews.length}/>
    </div>
    <div class="col-6 col-lg-3">
        <StatCard color="danger" icon="bi-exclamation-octagon" label="Reviews bloquantes" value={blocking}/>
    </div>
    <div class="col-6 col-lg-3">
        <StatCard color="success" icon="bi-clock-history" label="Dernière review" value={reviews[0]?.date ?? 'Aucune'}/>
    </div>
</div>

<Section icon="bi-folder2-open" title="Repos connectés">
    {#if connectedRepos.length === 0}
        <EmptyState message="Aucun repo connecté"/>
    {:else}
        <div class="row g-3">
            {#each connectedRepos as repo (repo.id)}
                <div class="col-md-6 col-lg-4">
                    <RepoCard {repo}/>
                </div>
            {/each}
        </div>
    {/if}
</Section>

<Section icon="bi-chat-square-text" title="Dernières reviews">
    <ReviewTable {reviews}/>
</Section>