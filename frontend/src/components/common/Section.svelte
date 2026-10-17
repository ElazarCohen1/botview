<script>
    export let title;
    export let icon = '';
</script>

<section class="mb-4">
    <h5 class="mb-3"><i class="bi {icon} me-2"></i>{title}</h5>
    <slot />
</section>