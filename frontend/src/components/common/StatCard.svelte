<script>
    export let label;
    export let value;
    export let icon = 'bi-bar-chart';
    export let color = 'primary';
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