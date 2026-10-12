
<script lang="ts">
    // import { providers } from '../../lib/mock.js';
    import  { GitProvider } from '../../types/Git';
    import {  user } from '../../lib/stores';
    import {saveToken} from "../../api/login/git";

    let {
        onClickAdd,
        disabledAddFormButton
    } = $props<{
        onClickAdd: () => void;
        disabledAddFormButton: boolean;
    }>();

    const providers = [
        GitProvider.GITHUB,
        GitProvider.GITLAB,
        GitProvider.BITBUCKET,
    ];

    let token = $state('');
    let loading = $state(false);
    let error = $state('');
    let success = $state(false);
    let id = 0;

    function showSuccess() {
        success = true;

        setTimeout(() => {
            success = false;
        }, 3000);
    }

    async function submit(event: SubmitEvent) {
        event.preventDefault();
        error = '';
        loading = true;
        try {
            // user.set(await saveToken($provider, token.trim()));
            token = '';
        } catch (e) {
            error = e instanceof Error ? e.message : 'Erreur inconnue.';
        } finally {
            loading = false;
            showSuccess();
        }
    }
</script>

<form class="d-flex flex-column align-items-end" onsubmit={submit}>
    <div class="input-group">
        <select class="form-select flex-grow-0" style="width: 9rem"  aria-label="Provider Git">
            {#each providers as p}
                <option value={p}>{GitProvider[p]}</option>
            {/each}
        </select>
        <input
                type="password"
                class="form-control"
                style="width: 18rem"
                placeholder="Token "
                autocomplete="off"
                bind:value={token}
        />
        <button class="btn btn-primary" type="submit" disabled={loading || !token.trim()}>
            {loading ? 'Ajout…' : 'Ajouter le token'}
        </button>

        <button
                type="button"
                class="btn btn-outline-primary"
                onclick={onClickAdd}
                disabled={disabledAddFormButton}
        >
            <i class="bi bi-plus-lg"></i>
        </button>
    </div>
    {#if error}
        <div class="text-danger large mt-1">{error}</div>
    {:else if success}
        <div class="text-success large mt-1">Token ajouté avec succes</div>
    {/if}

</form>