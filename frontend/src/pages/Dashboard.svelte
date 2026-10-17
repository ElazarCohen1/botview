<script lang="ts">
    import { connectedRepos, reviews } from "../lib/mock.js";
    import DashboardHeader from "../components/layout/DashboardHeader.svelte";
    import Section from "../components/common/Section.svelte";
    import StatCard from "../components/common/StatCard.svelte";
    import EmptyState from "../components/common/EmptyState.svelte";
    import RepoCard from "../components/repos/RepoCard.svelte";
    import ReviewTable from "../components/reviews/ReviewTable.svelte";
    import LoginForm from "../components/auth/LoginForm.svelte";
    import ProviderSelectButton from "../components/common/ProviderSelectButton.svelte";
    import { auth } from "../lib/stores..svelte.js";
    import { GitProviderEnum } from "../types/Git";
    import { getUser } from "../api/login/gitApi";

    const MAX_FORMS = 3;

    let loginForms = $state([0]);
    let nextId = 1;

    let canAdd = $derived(loginForms.length < MAX_FORMS);
    let blocking = $derived(
        reviews.filter((review) => review.blocking).length
    );

    let connexionProvider = $state<GitProviderEnum>(GitProviderEnum.GITHUB);
    let connecting = $state(false);
    let connectionError = $state("");

    function addLoginForm() {
        if (!canAdd) return;

        loginForms.push(nextId++);
    }

    function removeLoginForm(id: number) {
        loginForms = loginForms.filter((formId) => formId !== id);
    }

    async function login() {
        connecting = true;
        connectionError = "";

        try {
            const currentUser = await getUser(connexionProvider);
            auth.setUser(currentUser);
            if (!currentUser) {
                throw new Error(
                    "Impossible de récupérer l'utilisateur de ce provider."
                );
            }

        } catch (e) {
            connectionError = e instanceof Error
                ? e.message
                : "Erreur lors de la connexion.";
        } finally {
            connecting = false;
        }
    }
</script>

<DashboardHeader />

{#if !auth.isConnected}
    <EmptyState
            icon="bi-plug"
            message="Connecte-toi à un provider Git pour commencer."
    />

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

        <button
                type="button"
                class="btn btn-primary"
                onclick={login}
                disabled={connecting}
        >
            {connecting ? "Connexion…" : "Connexion"}
        </button>
    </div>

    {#if connectionError}
        <div class="text-danger text-center mt-2" role="alert">
            {connectionError}
        </div>
    {/if}
{:else}
    <div class="row g-3 mb-4">
        <div class="col-6 col-lg-3">
            <StatCard
                    label="Repos connectés"
                    value={connectedRepos.length}
                    icon="bi-git"
            />
        </div>
        <div class="col-6 col-lg-3">
            <StatCard
                    label="PR analysées"
                    value={reviews.length}
                    icon="bi-git"
                    color="info"
            />
        </div>
        <div class="col-6 col-lg-3">
            <StatCard
                    label="Reviews bloquantes"
                    value={blocking}
                    icon="bi-exclamation-octagon"
                    color="danger"
            />
        </div>
        <div class="col-6 col-lg-3">
            <StatCard
                    label="Dernière review"
                    value={reviews[0]?.date ?? "Aucune"}
                    icon="bi-clock-history"
                    color="success"
            />
        </div>
    </div>

    <Section title="Repos connectés" icon="bi-folder2-open">
        {#if connectedRepos.length === 0}
            <EmptyState message="Aucun repo connecté" />
        {:else}
            <div class="row g-3">
                {#each connectedRepos as repo (repo.id)}
                    <div class="col-md-6 col-lg-4">
                        <RepoCard {repo} />
                    </div>
                {/each}
            </div>
        {/if}
    </Section>

    <Section title="Dernières reviews" icon="bi-chat-square-text">
        <ReviewTable {reviews} />
    </Section>
{/if}