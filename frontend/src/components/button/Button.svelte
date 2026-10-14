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
    <button onclick={onClick} class="btn btn-outline-primary btn-sm">
        {text}
    </button>
</div>