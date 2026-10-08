<script>
    import { onMount } from 'svelte';

    let pullRequests = $state([]);
    let repoName = $state('');

    onMount(async () => {
        // Récupère le nom du repo passé dans le lien
        const hash = window.location.hash;
        const params = new URLSearchParams(hash.includes('?') ? hash.split('?')[1] : '');
        repoName = params.get('repo') || '';

        // Fait le fetch vers le backend
        if (repoName) {
            const res = await fetch(`/api/pullRequest/${repoName}`);
            if (res.ok) {
                pullRequests = await res.json();
            }
        }
    });
</script>

<div>
    <h2 class="mb-4">Pull Requests : {repoName}</h2>

    {#if pullRequests.length === 0}
        <div class="alert alert-secondary">
            Aucune Pull Request pour ce dépôt.
        </div>
    {:else}
        {#each pullRequests as pr}
            <div class="card p-3 mb-3 shadow-sm">
                <h5 class="card-title text-primary">#{pr.number} - {pr.title}</h5>
                <p class="card-text mb-2">
                    Statut : <span class="badge bg-success">{pr.state}</span>
                </p>
                <a href={pr.html_url} target="_blank" rel="noreferrer" class="btn btn-sm btn-outline-secondary">
                    Voir sur GitHub ↗
                </a>
            </div>
        {/each}
    {/if}
</div>
