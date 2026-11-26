<script lang="ts">
    import type {ReviewStatus} from "../../types/Review";
    import {STATUS} from "../../lib/reviewStatus";

    let {status}: { status: ReviewStatus } = $props();
</script>

<span class="badge text-bg-{STATUS[status].color}">{STATUS[status].label}</span>
