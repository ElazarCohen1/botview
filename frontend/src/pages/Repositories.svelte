<script lang="ts">
    import { onMount } from "svelte";
    import Button from "../components/button/Button.svelte";
    import RepoSelect from "../components/common/RepoSelect.svelte";
    import RepositoryItem from "../components/repos/RepositoryItem.svelte";
    import EmptyState from "../components/common/EmptyState.svelte";
    import { getWebHooks, createWebHook } from "../api/webhook/webHooks";
    import {
        getRepositoriesFromDb,
        addRepositoryToDb,
        deleteRepositoryFromDb,
    } from "../api/repositories/repositories";

    let selectedRepoName = $state("");
    let chosenRepos = $state([]);
    let resMessage = $state("");
    let loading = $state(true);
    async function refresh() {
        loading = true;
        const data = await getRepositoriesFromDb();
        if (data && data.length > 0) {
            chosenRepos = data;
        }
        loading = false;
    }
    function hasPullRequestWebHook(webHooks) {
        if (!webHooks) return false;
        return webHooks.some((hook) => hook.events?.includes("pull_request"));
    }
    async function addWebhook(repoName: string) {
        resMessage = "";
        const res = await createWebHook(repoName);
        if (!res.ok) {
            resMessage = res.error || "Erreur inconnue";
        }
        await refresh();
    }
    async function addSelectedRepo() {
        if (!selectedRepoName) return;
        resMessage = "";
        await addRepositoryToDb(selectedRepoName);
        const res = await createWebHook(selectedRepoName);
        if (!res.ok) {
            resMessage = res.error || "Erreur inconnue";
        }
        await refresh();
    }
    async function removeRepo(repoName: string) {
        await deleteRepositoryFromDb(repoName);
        await refresh();
    }

    onMount(refresh);
</script>

<div>
    <h2>Mes Dépôts</h2>
    {#if resMessage}
        <div class="alert alert-danger">{resMessage}</div>
    {/if}
    <div class="d-flex align-items-center gap-3 my-4">
        <label for="repo-select" class="fw-semibold">Choisir un dépôt :</label>
        <RepoSelect bind:bindValue={selectedRepoName} />
        <Button text="Ajouter à la liste" onClick={addSelectedRepo} />
    </div>
    <h4 class="mt-4 mb-3">Dépôts choisis ({chosenRepos.length})</h4>
    {#if loading && chosenRepos.length === 0}
        <p>Chargement...</p>
    {:else if chosenRepos.length === 0}
        <EmptyState
            message="Aucun dépôt affiché pour le moment. Choisissez-en un ci-dessus pour l'ajouter."
        />
    {:else}
        {#each chosenRepos as repo}
            {#await getWebHooks(repo.name) then dataWebhooks}
                <div class="mb-3">
                    <RepositoryItem {repo} />
                    {#if !hasPullRequestWebHook(dataWebhooks)}
                        <Button
                            text="Ajouter Webhook"
                            onClick={() => addWebhook(repo.name)}
                        />
                    {:else}
                        <p class="text-success mt-2">
                            Vous avez déjà un webhook
                        </p>
                    {/if}
                    <button
                        class="btn btn-sm btn-outline-danger"
                        onclick={() => removeRepo(repo.name)}
                    >
                        Supprimer de la liste
                    </button>
                </div>
            {/await}
        {/each}
    {/if}
</div>
