<script lang="ts">
    export let status;
    const colors: Record<string, string> = {
        terminée: "success",
        "en cours": "primary",
        "en attente": "secondary",
        échec: "danger",
    };
</script>

<span class="badge text-bg-{colors[status] ?? 'secondary'}">{status}</span>
