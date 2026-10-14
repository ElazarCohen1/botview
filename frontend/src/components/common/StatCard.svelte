<script lang="ts">
    let {
        label,
        value,
        icon = 'bi-bar-chart',
        color = 'primary'
    } = $props<{
        label: string;
        value: string | number;
        icon?: string;
        color?: string;
    }>();
</script>

<div class="card h-100">
    <div class="card-body d-flex align-items-center">
        <i class="bi {icon} fs-1 text-{color} me-3"></i>
        <div>
            <div class="fs-3 fw-bold">{value}</div>
            <div class="text-body-secondary small">{label}</div>
        </div>
    </div>
</div>