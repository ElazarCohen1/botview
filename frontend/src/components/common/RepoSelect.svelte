<script lang="ts">
    import {onMount} from "svelte";
    import {getGitRepositories} from "../../api/repositories/repositories";
    import type {Repository} from "../../types/Repository";
    import {gitProvider} from "../../lib/stores.svelte";

    let {bindValue = $bindable(null)}: {
        bindValue: Repository | null;
    } = $props();

    let repos = $state<Repository[]>([]);
    onMount(async () => {
        const data = await getGitRepositories(gitProvider.selected!);
        if (data && data.length > 0) {
            repos = data;
            if (!bindValue) {
                bindValue = data[0];
            }
        }
    });
</script>

<select bind:value={bindValue} class="form-select" style="width: 22rem">
    <option disabled value="">Choisir un repo…</option>
    {#each repos as r}
        <option value={r}>{r.full_name || r.name}</option>
    {/each}
</select>
