<script lang="ts">
    let {
        onClick,
        text
    } = $props<{
        onClick: () => void;
        text: string;
    }>();
</script>


<div class="d-flex justify-content-between align-items-center mb-4">
    <button class="btn btn-outline-primary btn-sm" onclick={onClick}>
        {text}
    </button>
</div>