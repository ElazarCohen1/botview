<script lang="ts">
    let {
        message,
        onClick,
        style
    }: {
        message: string;
        onClick: () => void;
        style: string;
    }
        = $props();

</script>

<button class="btn btn-outline-primary btn-sm" onclick={onClick}>
    {message}
</button>

