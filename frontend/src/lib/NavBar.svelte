<script lang="ts">
    import RepoSelector from "../components/repos/RepoSelector.svelte";
    import UserBadge from "../components/auth/UserBadge.svelte";
    import { auth, gitProvider } from "./stores.svelte";
    import { GitProviderEnum } from "../types/Git";
    import ProviderSelectButton from "../components/common/ProviderSelectButton.svelte";
    import { getUser } from "../api/login/gitApi";

    let providerState = $state<GitProviderEnum>(
        gitProvider.selected ?? GitProviderEnum.GITHUB,
    );

    $effect(() => {
        gitProvider.select(providerState);
    });

    $effect(() => {
        const provider = providerState;
        let cancelled = false;

        if (provider === null) return;

        getUser(provider)
            .then((user) => {
                if (!cancelled) {
                    auth.setUser(user);
                }
            })
            .catch((error) => {
                if (!cancelled) {
                    console.error("Erreur de chargement", error);
                }
            });

        return () => {
            cancelled = true;
        };
    });
</script>

<nav
    class="navbar navbar-expand-lg bg-body-tertiary fixed-top border-bottom top-navbar"
>
    <div class="container-fluid gap-3 d-flex justify-content-end">
        <div class="d-flex flex-wrap align-items-center gap-3">
            {#if auth.isConnected}
                <ProviderSelectButton bind:bindValue={providerState} />
                <UserBadge />
            {/if}
        </div>
        <a
            class="nav-link fs-5"
            href="#/settings"
            title="Paramètres"
            aria-label="Paramètres"
        >
            ⚙<span class="visually-hidden">Paramètres</span>
        </a>
    </div>
</nav>

<style>
    .top-navbar {
        left: 250px;
    }
</style>
