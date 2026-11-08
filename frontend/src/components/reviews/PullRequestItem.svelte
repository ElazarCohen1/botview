<script lang="ts">
    import type {PullRequest} from "../../types/PullRequest";

    let {
        pr
    }: {
        pr: PullRequest
    } = $props();


</script>
<div class="card p-3 mb-3 shadow-sm">
    <h5 class="card-title text-primary">
        #{pr.number} - {pr.title}
    </h5>
    <p class="card-text mb-2">
        Statut : <span class="badge bg-success">{pr.state}</span>
    </p>
    <a
            class="btn btn-sm btn-outline-secondary"
            href={pr.html_url}
            rel="noreferrer"
            target="_blank"
    >
        Voir sur GitHub ↗
    </a>
</div>
