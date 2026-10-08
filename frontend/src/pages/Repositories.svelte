<script>
    import { onMount } from 'svelte';
    let repos = $state([]);
    onMount(async () => {
        const res = await fetch('/api/repositories');
        if (res.ok) {
            repos = await res.json();
        }
    });
</script>
<div>
    <h2 class="mb-4">Repositories</h2>
    {#each repos as repo}
        <div class="card p-3 mb-3 shadow-sm">
            <h5 class="card-title text-primary">{repo.name}</h5>
            <p class="card-text text-muted mb-2">
                Branche : <code>{repo.default_branch}</code>
            </p>
            <div>
                <a href="#/pull-requests?repo={repo.name}" class="btn btn-sm btn-primary me-2">
                    Voir les Pull Requests
                </a>
                <a href={repo.html_url} target="_blank" rel="noreferrer" class="btn btn-sm btn-outline-secondary">
                    GitHub ↗
                </a>
            </div>
        </div>
    {/each}
</div>
