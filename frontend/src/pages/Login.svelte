<script lang="ts">
    import { push } from 'svelte-spa-router';
    import EmptyState from '../components/common/EmptyState.svelte';
    import LoginForm from '../components/auth/LoginForm.svelte';
    import ProviderSelectButton from '../components/common/ProviderSelectButton.svelte';
    import { auth } from '../lib/stores..svelte'
    import { GitProviderEnum } from '../types/Git';
    import { getUser } from '../api/login/gitApi';

    const MAX_FORMS = 3;

    let loginForms = $state([0]);
    let nextId = 1;
    let canAdd = $derived(loginForms.length < MAX_FORMS);

    let connexionProvider = $state<GitProviderEnum>(GitProviderEnum.GITHUB);
    let connecting = $state(false);
    let connectionError = $state('');

    // Déjà connecté ? On ne reste pas sur /login
    $effect(() => {
        if (auth.isConnected) push('/');
    });

    function addLoginForm() {
        if (canAdd) loginForms.push(nextId++);
    }

    function removeLoginForm(id: number) {
        loginForms = loginForms.filter((formId) => formId !== id);
    }

    async function login() {
        connecting = true;
        connectionError = '';
        try {
            const currentUser = await getUser(connexionProvider);
            if (!currentUser) {
                throw new Error("Impossible de récupérer l'utilisateur de ce provider.");
            }
            auth.setUser(currentUser);
        } catch (e) {
            connectionError = e instanceof Error ? e.message : 'Erreur lors de la connexion.';
        } finally {
            connecting = false;
        }
    }
</script>

<div class="container" >
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <h3 class="text-center mb-4">BotView</h3>

            <EmptyState icon="bi-plug" message="Connecte-toi à un provider Git pour commencer." />

            {#each loginForms as id, i (id)}
                <div class="mb-3">
                    <LoginForm
                            isFirst={i === 0}
                            {canAdd}
                            onAdd={addLoginForm}
                            onRemove={() => removeLoginForm(id)}
                    />
                </div>
            {/each}

            <div class="d-flex justify-content-center align-items-center gap-2 mt-4">
                <ProviderSelectButton bind:bindValue={connexionProvider} />
                <button type="button" class="btn btn-primary" onclick={login} disabled={connecting}>
                    {connecting ? 'Connexion…' : 'Connexion'}
                </button>
            </div>

            {#if connectionError}
                <div class="text-danger text-center mt-2" role="alert">{connectionError}</div>
            {/if}
        </div>
    </div>
</div>