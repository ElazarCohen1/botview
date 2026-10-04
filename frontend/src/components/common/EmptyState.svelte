<script>
    export let message;
    export let icon = 'bi-inbox';
</script>

<div class="text-center text-body-secondary border rounded p-4">
    <i class="bi {icon} fs-2 d-block mb-2"></i>
    {message}
</div>