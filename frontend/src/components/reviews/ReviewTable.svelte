<script>
    import StatusBadge from './StatusBadge.svelte';
    import EmptyState from '../common/EmptyState.svelte';
    export let reviews = [];
</script>

{#if reviews.length === 0}
    <EmptyState message="Aucune review pour l'instant" />
{:else}
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead>
            <tr><th>PR</th><th>Repo</th><th>Statut</th><th>Commentaires</th><th>Date</th></tr>
            </thead>
            <tbody>
            {#each reviews as r (r.id)}
                <tr>
                    <td>
                        {r.pr}
                        {#if r.blocking}<span class="badge text-bg-danger ms-1">bloquante</span>{/if}
                    </td>
                    <td>{r.repo}</td>
                    <td><StatusBadge status={r.status} /></td>
                    <td>{r.comments}</td>
                    <td>{r.date}</td>
                </tr>
            {/each}
            </tbody>
        </table>
    </div>
{/if}