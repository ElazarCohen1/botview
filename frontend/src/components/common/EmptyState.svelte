<script lang="ts">
    let {
        message,
        icon = 'bi-inbox'
    } = $props<{
        message: string;
        icon?: string;
    }>();
</script>

<div class="text-center text-body-secondary border rounded p-4">
    <i class="bi {icon} fs-2 d-block mb-2"></i>
    {message}
</div>