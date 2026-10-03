<script>
    import { onMount } from 'svelte';

    let user = $state(null);
    let loading = $state(true);

    onMount(async () => {
        try {
            const res = await fetch('/api/me');
            if (res.ok) {
                user = await res.json();
            }
        } catch (e) {
            console.error('Erreur réseau', e);
        } finally {
            loading = false;
        }
    });
</script>

{#if loading}
    <p>Chargement...</p>

{:else if user}
    <div>
        <img src={user.attributes.avatarUrl} alt="avatar" width="48" height="48" />
        <p>Connecté en tant que <strong>{user.login}</strong></p>
        <!-- ici : repos récents, dernières reviews, etc. -->
    </div>

{:else}
    <div>
        <button onclick={() => window.location.href = "/oauth/login/github"}>
            Se connecter avec GitHub
        </button>
        <p>
            Bienvenue dans le dashboard pour avoir une vue d'ensemble sur
            les comptes GitHub, les repos récents, la connexion à de nouveaux repos
            et les infos de base sur les dernières reviews.
        </p>
    </div>
{/if}