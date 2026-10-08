<script lang="ts">
    import { providers } from '../../lib/mock.js';
    import { provider } from '../../lib/stores.js';
    import type { GitUserDto } from '../../types/Gituser.js';
    import { login } from '../../api/login/login.js';
    import {user} from '../../lib/stores'

    let token  = $state('');
    let loading:boolean = $state(false);

    async function loginFct() {
        try{
            loading = true;
            const gitUser = await login();
            user.set(gitUser);
        }catch (e) {
            console.error("error fetching the user");
        }finally {
            loading = false;
        }
    }
</script>

<div class="btn-group">
    <button class="btn btn-light" onclick={loginFct}>
        {#if loading}
            Chargement...
        {:else}
                <i class="bi bi-{$provider.toLowerCase()} me-2"></i>Connexion {$provider}
        {/if}
    </button>


    <button class="btn btn-light dropdown-toggle dropdown-toggle-split"
            data-bs-toggle="dropdown" aria-label="Changer de provider"></button>
    <ul class="dropdown-menu dropdown-menu-end">
        {#each providers as p}
            <li>
                <button class="dropdown-item" class:active={p === $provider}
                        onclick={() => provider.set(p)}>{p}</button>
            </li>
        {/each}
    </ul>
</div>