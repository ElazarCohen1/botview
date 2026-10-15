<script lang="ts">
    import type { Repository } from "../../types/Repository";

    let { repo } = $props<{
        repo: Repository;
    }>();
</script>

<div class="card p-3 mb-3 shadow-sm">
    <h5 class="card-title text-primary">{repo.name}</h5>
    <p class="card-text text-muted mb-2">
        Branche : <code>{repo.default_branch}</code>
    </p>
    <div>
        <a
            href="#/pull-requests?repo={repo.name}"
            class="btn btn-sm btn-primary me-2"
        >
            Voir les Pull Requests
        </a>
        <a
            href={repo.html_url}
            target="_blank"
            rel="noreferrer"
            class="btn btn-sm btn-outline-secondary"
        >
            GitHub ↗
        </a>
    </div>
</div>
