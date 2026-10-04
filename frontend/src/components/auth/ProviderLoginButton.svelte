<script>
    import { providers } from '../../lib/mock.js';
    import { provider } from '../../lib/stores.js';

    function login() {
        // Plus tard : window.location.href = `/oauth/login/${$provider.toLowerCase()}`
        alert(`Connexion à ${$provider}…`);
    }
</script>

<div class="btn-group">
    <button class="btn btn-light" on:click={login}>
        <i class="bi bi-{$provider.toLowerCase()} me-2"></i>Connexion {$provider}
    </button>
    <button class="btn btn-light dropdown-toggle dropdown-toggle-split"
            data-bs-toggle="dropdown" aria-label="Changer de provider"></button>
    <ul class="dropdown-menu dropdown-menu-end">
        {#each providers as p}
            <li>
                <button class="dropdown-item" class:active={p === $provider}
                        on:click={() => provider.set(p)}>{p}</button>
            </li>
        {/each}
    </ul>
</div>