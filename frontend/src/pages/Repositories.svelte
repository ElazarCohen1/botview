<script lang="ts">
    import { onMount } from "svelte";
    import { getRepositories } from "../api/repositories/repositories";
    import Button from "../components/button/Button.svelte";
    import RepositoryItem from "../components/repos/RepositoryItem.svelte";
    let repos = $state([]);
    let loading = $state(true);
    let error = $state("");

    async function refresh() {
        loading = true;
        error = "";
        try {
            const data = await getRepositories();
            if (data) {
                repos = data;
            } else {
                error = "Impossible de recuperer les depots";
            }
        } catch (e) {
            error = "Impossible de contacter le serveur";
        } finally {
            loading = false;
        }
    }

    onMount(async () => {
        refresh();
    });
    async function addRepos() {}
</script>

<div>
    <h2>Dépots</h2>
    <Button text="Rafraichir" onClick={refresh} />
    {#if loading}
        <p>Chargement des dépôts...</p>
    {:else if error}
        <div class="alert alert-danger">
            {error}
        </div>
    {:else if repos.length === 0}
        <div class="alert alert-secondary">Aucun dépôt trouvé.</div>
    {:else}
        {#each repos as repo}
            <RepositoryItem {repo} />
            <Button text="Ajouter" onClick={addRepos} />
        {/each}
    {/if}
</div>
