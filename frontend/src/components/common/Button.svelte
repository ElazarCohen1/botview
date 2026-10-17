<script lang="ts">
    let {
        message,
        onClick,
        style
    }: {
        message: string;
        onClick : () => void;
        style: string;
    }
    = $props();

</script>

<button onclick={onClick} class="btn btn-outline-primary btn-sm">
    {message}
</button>

