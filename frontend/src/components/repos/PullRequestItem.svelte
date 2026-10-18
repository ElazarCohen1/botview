<script lang="ts">
    import type { PullRequest } from "../../types/PullRequest";
    export let pr: PullRequest;
</script>

<div class="card p-3 mb-3 shadow-sm">
    <h5 class="card-title text-primary">#{pr.number} - {pr.title}</h5>
    <p class="card-text mb-2">
        Statut : <span class="badge bg-success">{pr.state}</span>
    </p>
    <a
        href={pr.html_url}
        target="_blank"
        rel="noreferrer"
        class="btn btn-sm btn-outline-secondary"
    >
        Voir sur GitHub ↗
    </a>
</div>
