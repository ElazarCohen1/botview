<script lang="ts">
    import {onMount} from "svelte";
    import RepoSelect from "../components/common/RepoSelect.svelte";
    import {addRepository, getRepositories, removeWebhook} from "../api/repositories/repositories";
    import type {Repository} from "../types/Repository";
    import {gitProvider} from "../lib/stores.svelte";
    import EmptyState from "../components/common/EmptyState.svelte";
    import RepositoryItem from "../components/repos/RepositoryItem.svelte";

    let selectedRepo: Repository | null = $state(null);
    let monitoredRepos: Repository[] = $state([]);
    let resMessage: string = $state("");
    let loading: boolean = $state(true);
    let busy: boolean = $state(false);

    let selectedAlreadyMonitored = $derived(
        selectedRepo !== null &&
        monitoredRepos.some(r => r.name === selectedRepo!.full_name || r.url === selectedRepo!.url)
    );

    function errorMessage(e: unknown): string {
        return e instanceof Error ? e.message : "Une erreur inattendue est survenue.";
    }

    async function loadRepos() {
        loading = true;
        try {
            const all = await getRepositories(gitProvider.selected!);
            monitoredRepos = all.filter(r => r.has_webhook);
        } catch (e) {
            resMessage = errorMessage(e);
        } finally {
            loading = false;
        }
    }

    async function addWebhook(repo: Repository) {
        resMessage = "";
        busy = true;
        try {
            await addRepository(repo.full_name, gitProvider.selected!);
            await loadRepos();
            selectedRepo = null;
        } catch (e) {
            resMessage = errorMessage(e);
        } finally {
            busy = false;
        }
    }

    async function deleteWebhook(repo: Repository) {
        resMessage = "";
        busy = true;
        try {
            await removeWebhook(repo.id.toString(), gitProvider.selected!);
            await loadRepos();
        } catch (e) {
            resMessage = errorMessage(e);
        } finally {
            busy = false;
        }
    }

    onMount(loadRepos);
</script>

<div>
    <h2>Mes Dépôts</h2>

    {#if resMessage}
        <div class="alert alert-danger">{resMessage}</div>
    {/if}

    <section class="mb-5">
        <h4 class="mt-4 mb-3">Ajouter un dépôt</h4>
        <div class="d-flex align-items-center gap-3 mb-3">
            <label class="fw-semibold" for="repo-select">Choisir un dépôt :</label>
            <RepoSelect bind:bindValue={selectedRepo}/>
        </div>

        {#if selectedRepo}
            {#if selectedAlreadyMonitored}
                <div class="alert alert-info">Ce dépôt a déjà un webhook actif.</div>
            {:else}
                <RepositoryItem
                        repo={selectedRepo}
                        hasWebhook={false}
                        {busy}
                        onAddWebhook={() => addWebhook(selectedRepo!)}
                />
            {/if}
        {/if}
    </section>

    <section>
        <h4 class="mb-3">Webhook actif ({monitoredRepos.length})</h4>
        {#if loading && monitoredRepos.length === 0}
            <p>Chargement...</p>
        {:else if monitoredRepos.length === 0}
            <EmptyState
                    message="Aucun dépôt surveillé pour le moment. Choisissez-en un ci-dessus pour activer un webhook."/>
        {:else}
            {#each monitoredRepos as repo (repo.id)}
                <RepositoryItem
                        {repo}
                        hasWebhook={true}
                        {busy}
                        onRemoveWebhook={() => deleteWebhook(repo)}
                />
            {/each}
        {/if}
    </section>
</div>