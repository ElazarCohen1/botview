<script>
    import { onMount } from 'svelte';

    let user = $state(null);
    let token = $state('');
    let loading = $state(true);
    let error = $state('');

    onMount(async () => {
        try {
            const res = await fetch('/api/user');

            if (res.ok) {
            }
        } catch (e) {
            console.error('Erreur réseau', e);
        } finally {
            loading = false;
        }
    });

    async function login() {
        error = '';

        try {
            const res = await fetch('/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    token: token
                })
            });

            if (res.ok) {
                user = await res.json();
                token = '';
            } else {
                error = 'Token GitHub invalide.';
            }
        } catch (e) {
            console.error('Erreur réseau', e);
            error = 'Impossible de contacter le serveur.';
        }
    }
</script>

{#if loading}

    <p>Chargement...</p>

{:else if user}

    <div>
        <img
                src={user.avatar_url}
                alt="Avatar"
                width="48"
                height="48"
        />

        <p>
            Connecté en tant que <strong>{user.login}</strong>
        </p>
    </div>

{:else}

    <div>
        <h2>Connexion à GitHub</h2>

        <input
                type="password"
                bind:value={token}
                placeholder="Votre token GitHub"
        />

        <button onclick={login}>
            Se connecter
        </button>

        {#if error}
            <p>{error}</p>
        {/if}

        <p>
            Bienvenue dans le dashboard pour avoir une vue d'ensemble sur
            les comptes GitHub, les repos récents, la connexion à de nouveaux repos
            et les informations de base sur les dernières reviews.
        </p>
    </div>

{/if}