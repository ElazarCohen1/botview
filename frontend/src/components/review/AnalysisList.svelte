<script lang="ts">
    import type {Analysis} from "../../types/Review";
    import ReviewStatusBadge from "./ReviewStatusBadge.svelte";

    let {analyses}: { analyses: Analysis[] } = $props();
</script>

<ul class="list-group">
    {#each analyses as analysis (analysis.id)}
        <li class="list-group-item">
            <div class="d-flex justify-content-between align-items-center">
                <span class="fw-semibold">{analysis.tool}</span>
                <ReviewStatusBadge status={analysis.status}/>
            </div>
            {#if analysis.result}
                <pre class="small mt-2 mb-0 text-wrap">{analysis.result}</pre>
            {/if}
        </li>
    {:else}
        <li class="list-group-item text-body-secondary">Aucune analyse lancée pour l'instant.</li>
    {/each}
</ul>
