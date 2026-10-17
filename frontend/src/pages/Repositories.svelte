<script lang="ts">
    import { onMount } from "svelte";
    import { getRepositories } from "../api/repositories/repositories";
    import { getWebHooks } from "../api/webhook/webHooks";
    import Button from "../components/button/Button.svelte";
    import RepositoryItem from "../components/repos/RepositoryItem.svelte";
    let repos = $state([]);
    let dataWebhooks = $state([]);
    let loading = $state(true);
    let error = $state("");

    async function refresh() {
        loading = true;
        error = "";
        try {

            const data = await getRepositories();
            if (data ) {
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

    function  hasPullRequestWebHook(webHooks){
        if(!webHooks){
            return false;
        }
        for(const hook of webHooks){
            if(hook.events.includes("pull_request")) {
                return true;
            }
        }
        return false;
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
            {#await getWebHooks(repo.name)}
            {:then dataWebhooks}
                {#if !hasPullRequestWebHook(dataWebhooks)}
                <RepositoryItem {repo} />
                <Button text="Ajouter" onClick={addRepos} />
                {:else}
                    <RepositoryItem {repo} />
                    <p> Vous avez deja un webhook</p>
                {/if}

            {:catch  e}
    {/await}


        {/each}
    {/if}
</div>