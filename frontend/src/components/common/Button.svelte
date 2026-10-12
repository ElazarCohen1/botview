<script>
    export let message  = '';
    export let onClick;
    export let style;
</script>

<button onclick={onClick} class="btn btn-outline-primary btn-sm">
    {message}
</button>

