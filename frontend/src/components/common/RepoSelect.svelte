<script lang="ts">
    import { onMount } from "svelte";
    import { getRepositories } from "../../api/repositories/repositories";
    import type { Repository } from "../../types/Repository";
    let { bindValue = $bindable("") } = $props<{
        bindValue: string;
    }>();
    let repos = $state<Repository[]>([]);
    onMount(async () => {
        const data = await getRepositories();
        if (data && data.length > 0) {
            repos = data;
            if (!bindValue) {
                bindValue = data[0].name;
            }
        }
    });
</script>
<select class="form-select" style="width: 22rem" bind:value={bindValue}>
    <option value="" disabled>Choisir un repo…</option>
    {#each repos as r}
        <option value={r.name}>{r.name}</option>
    {/each}
</select>