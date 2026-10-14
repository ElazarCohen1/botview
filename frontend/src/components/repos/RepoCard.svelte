<script lang="ts">
    interface ConnectedRepo {
        id?: number;
        name: string;
        origin?: string;
        prCount?: number;
        webhook?: string;
    }

    let {
        repo
    } = $props<{
        repo: ConnectedRepo;
    }>();

    let webhookColor = $derived(repo.webhook === 'actif' ? 'success' : 'danger');
</script>

<div class="card h-100">
    <div class="card-body">
        <div class="d-flex justify-content-between">
            <h6 class="card-title">{repo.name}</h6>
            <span class="badge text-bg-secondary">{repo.origin}</span>
        </div>
        <p class="text-body-secondary small mb-2">{repo.prCount} PR analysées</p>
        <span class="badge text-bg-{webhookColor}">Webhook {repo.webhook}</span>
    </div>
</div>