<script lang="ts">
    import type {Analysis} from "../../types/Review";
    import {STATUS} from "../../lib/reviewStatus";

    let {analyses}: { analyses: Analysis[] } = $props();
</script>

<div class="d-flex gap-2">
    {#each analyses as analysis (analysis.id)}
        {@const text = `${analysis.tool} : ${STATUS[analysis.status].label}`}
        <span
                aria-label={text}
                class="dot bg-{STATUS[analysis.status].color}"
                class:running={analysis.status === "RUNNING"}
                role="img"
                title={text}
        ></span>
    {/each}
</div>

<style>
    .dot {
        width: 14px;
        height: 14px;
        border-radius: 50%;
        transition: background-color 0.4s;
    }

    .running {
        animation: pulse 1s ease-in-out infinite alternate;
    }

    @keyframes pulse {
        from { opacity: 1; transform: scale(1); }
        to { opacity: 0.4; transform: scale(0.8); }
    }
</style>
