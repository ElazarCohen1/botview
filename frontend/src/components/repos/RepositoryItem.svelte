<script lang="ts">
    import type {Repository} from "../../types/Repository";

    let {
        repo,
        hasWebhook = false,
        busy = false,
        onAddWebhook,
        onRemoveWebhook
    }: {
        repo: Repository;
        hasWebhook?: boolean;
        busy?: boolean;
        onAddWebhook?: () => void;
        onRemoveWebhook?: () => void;
    } = $props();
</script>

<div class="card p-3 mb-3 shadow-sm">
    <div class="d-flex justify-content-between align-items-start">
        <h5 class="card-title text-primary">{repo.name ?? repo.full_name}</h5>
        {#if hasWebhook}
            <span class="badge text-bg-success">Webhook actif</span>
        {:else}
            <span class="badge text-bg-secondary">Pas de webhook</span>
        {/if}
    </div>

    {#if repo.branch}
        <p class="card-text text-muted mb-2">
            Branche : <code>{repo.branch}</code>
        </p>
    {/if}

    <div class="d-flex flex-wrap gap-2">
        <a
                class="btn btn-sm btn-primary"
                href="#/pull-requests?repo={repo.id}"
        >
            Voir les Pull Requests
        </a>
        <a
                class="btn btn-sm btn-outline-secondary"
                href={repo.url}
                rel="noreferrer"
                target="_blank"
        >
            GitHub ↗
        </a>

        {#if hasWebhook}
            <button
                    class="btn btn-sm btn-outline-danger ms-auto"
                    disabled={busy}
                    onclick={onRemoveWebhook}
            >
                Supprimer le webhook
            </button>
        {:else}
            <button
                    class="btn btn-sm btn-success ms-auto"
                    disabled={busy}
                    onclick={onAddWebhook}
            >
                Activer le webhook
            </button>
        {/if}
    </div>
</div>