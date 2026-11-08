<script lang="ts">
    import {GitProviderEnum} from "../../types/Git";
    import {saveToken} from "../../api/login/gitApi";
    import ProviderSelectButton from "../common/ProviderSelectButton.svelte";
    import {gitProvider} from "../../lib/stores.svelte";

    let {
        isFirst,
        canAdd,
        onAdd,
        onRemove
    }: {
        isFirst: boolean;
        canAdd: boolean;
        onAdd: () => void;
        onRemove: () => void;
    } = $props();

    let provider = $state<GitProviderEnum>(GitProviderEnum.GITHUB);
    let token = $state("");
    let loading = $state(false);
    let error = $state("");
    let success = $state(false);

    async function submit(event: SubmitEvent) {
        event.preventDefault();

        error = "";
        success = false;
        loading = true;

        try {
            await saveToken(provider, token.trim());
            gitProvider.select(provider);

            token = "";
            success = true;

            setTimeout(() => {
                success = false;
            }, 3000);
        } catch (e) {
            error = e instanceof Error
                ? e.message
                : "Erreur lors de l'enregistrement du token.";
        } finally {
            loading = false;
        }
    }
</script>

<form class="d-flex flex-column align-items-end" onsubmit={submit}>
    <div class="input-group">
        <ProviderSelectButton bind:bindValue={provider}/>

        <input
                autocomplete="off"
                bind:value={token}
                class="form-control"
                disabled={loading}
                placeholder="Token"
                required
                style="width: 18rem"
                type="password"
        />

        <button
                class="btn btn-primary"
                disabled={loading || !token.trim()}
                type="submit"
        >
            {loading ? "Ajout…" : "Ajouter le token"}
        </button>

        <button
                aria-label={isFirst
                ? "Ajouter un provider"
                : "Supprimer ce provider"}
                class="btn btn-outline-primary"
                disabled={isFirst && !canAdd}
                onclick={isFirst ? onAdd : onRemove}
                type="button"
        >
            <i class="bi {isFirst ? 'bi-plus-lg' : 'bi-dash-lg'}"></i>
        </button>
    </div>

    {#if error}
        <div class="text-danger mt-1" role="alert">
            {error}
        </div>
    {:else if success}
        <div class="text-success mt-1" role="status">
            Token enregistré avec succès.
        </div>
    {/if}
</form>