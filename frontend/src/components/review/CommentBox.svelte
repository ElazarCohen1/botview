<script lang="ts">
    import type {LineComment} from "../../types/Review";

    let {comment, showLine = false}: { comment: LineComment; showLine?: boolean } = $props();
</script>

<div class="alert alert-warning m-2 py-2" style="font-family: var(--bs-body-font-family)">
    {#if showLine}
        <strong>Ligne {comment.line} :</strong>
    {/if}
    {comment.comment}
</div>
